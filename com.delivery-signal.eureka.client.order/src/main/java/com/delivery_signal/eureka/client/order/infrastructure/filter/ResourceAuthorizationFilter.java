package com.delivery_signal.eureka.client.order.infrastructure.filter;

import com.delivery_signal.eureka.client.order.domain.vo.user.UserAuthorizationInfo;
import com.delivery_signal.eureka.client.order.infrastructure.client.user.UserClient;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * ✅ 각 MSA별 공통 인가 필터 (복붙용)
 *
 * <p>게이트웨이에서 JWT 검증 후 x-user-id 헤더를 전달받음.
 * 이후 이 필터에서 User 서비스 호출 → 활성 사용자 여부 + Role 검증 수행.
 *
 * <p>서비스별로 권한 정책이 다를 경우 hasPermission() 내부만 수정해서 사용.
 */
@Slf4j
@Component
@Order(2) // AuthenticationFilter 이후 실행
public class ResourceAuthorizationFilter implements WebFilter {

    private static final String USER_ID_HEADER = "x-user-id";
    private static final String INTERNAL_CALL_HEADER = "x-internal-call";

    private final UserClient userClient;

    public ResourceAuthorizationFilter(UserClient userClient) {
        this.userClient = userClient;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        // 🔹 내부 호출은 인가 스킵
        String internalCall = exchange.getRequest().getHeaders().getFirst(INTERNAL_CALL_HEADER);
        if ("true".equalsIgnoreCase(internalCall)) {
            return chain.filter(exchange);
        }

        // 🔹 게이트웨이에서 전달된 userId 헤더 확인
        String userIdHeader = exchange.getRequest().getHeaders().getFirst(USER_ID_HEADER);
        if (userIdHeader == null) {
            log.warn("❌ x-user-id 헤더 누락");
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        Long userId;
        try {
            userId = Long.parseLong(userIdHeader);
        } catch (NumberFormatException e) {
            log.warn("❌ 잘못된 userId 형식: {}", userIdHeader);
            exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
            return exchange.getResponse().setComplete();
        }

        // 🔹 FeignClient 호출 (UserService)
        UserAuthorizationInfo userInfo;
        try {
            userInfo = userClient.getUserAuthorizationInfo(userId);
        } catch (FeignException.NotFound e) {
            log.warn("❌ 존재하지 않는 사용자: userId={}", userId);
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        } catch (Exception e) {
            log.error("❌ User 서비스 호출 실패: {}", e.getMessage());
            exchange.getResponse().setStatusCode(HttpStatus.SERVICE_UNAVAILABLE);
            return exchange.getResponse().setComplete();
        }

        // 🔹 활성 여부 확인
        if (userInfo == null || !userInfo.isActive()) {
            log.warn("❌ 비활성 사용자: userId={}", userId);
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        // 🔹 HTTP 메서드 확인
        HttpMethod method = exchange.getRequest().getMethod();
        if (method == null) {
            exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
            return exchange.getResponse().setComplete();
        }

        // 🔹 권한 정책 확인
        if (!hasPermission(userInfo, method, exchange)) {
            log.warn("🚫 접근 거부: userId={} role={} method={}", userId, userInfo.role(), method);
            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
            return exchange.getResponse().setComplete();
        }

        // ✅ 통과
        return chain.filter(exchange);
    }

    /**
     * 🔹 URL에서 리소스 ID 추출
     * 예: /orders/{orderId}/status → segments[2] = orderId
     */
    private String extractResourceId(ServerWebExchange exchange) {
        String path = exchange.getRequest().getURI().getPath();
        String[] segments = path.split("/");
        return segments.length >= 3 ? segments[2] : null;
    }

    /**
     * 🔹 권한 검사 로직
     *
     * ❗각 서비스는 이 부분만 커스터마이징해서 사용하면 됨
     *
     * - MASTER: 모든 요청 허용
     * - HUB_MANAGER: GET/POST 허용
     * - DELIVERY_MANAGER: GET만 허용
     * - SUPPLIER_MANAGER: 본인 소유 리소스만 허용
     */
    protected boolean hasPermission(UserAuthorizationInfo user, HttpMethod method, ServerWebExchange exchange) {
        String role = user.role();
        String resourceId = extractResourceId(exchange);

        return switch (role) {
            case "MASTER" -> true;
            case "HUB_MANAGER" -> HttpMethod.GET.equals(method) || HttpMethod.POST.equals(method);
            case "DELIVERY_MANAGER" -> HttpMethod.GET.equals(method);
            case "SUPPLIER_MANAGER" -> resourceId != null && validateSupplierResource(user, resourceId);
            default -> false;
        };
    }

    /**
     * 🔹 SUPPLIER_MANAGER 리소스 소유권 검증
     * - 각 서비스에서 실제 로직으로 교체 가능 (예: DB 조회 등)
     */
    protected boolean validateSupplierResource(UserAuthorizationInfo user, String resourceId) {
        log.debug("임시 리소스 검증 (true 반환) userId={} resourceId={}", user.userId(), resourceId);
        return true;
    }
}
