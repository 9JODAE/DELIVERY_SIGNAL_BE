package com.delivery_signal.eureka.client.delivery.infrastructure.filter;

import com.delivery_signal.eureka.client.delivery.domain.vo.AuthorizedUser;
import com.delivery_signal.eureka.client.delivery.infrastructure.adapter.UserServiceAdapter;
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
 * 인가 필터: User Service 호출을 통해 상세 권한을 획득하고 UserContextHolder에 저장
 */
@Slf4j
@Component
@Order(2) // AuthenticationFilter (Order 1) 다음에 실행
public class ResourceAuthorizationFilter implements WebFilter {

    // 클라이언트에서 헤더를 통해 전달되는 유저 ID
    private static final String USER_ID_HEADER = "X-User-Id";
    private final UserServiceAdapter userServiceAdapter;

    public ResourceAuthorizationFilter(UserServiceAdapter userServiceAdapter) {
        this.userServiceAdapter = userServiceAdapter;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        // 내부 호출 확인
        // /open-api/로 시작하는 URL은 서비스 내부 호출로 간주, 권한 체크 생략
        String path = exchange.getRequest().getURI().getPath();
        if (path.startsWith("/open-api/")) {
            return chain.filter(exchange);
        }

        // 외부 호출 확인
        String userIdHeader = exchange.getRequest().getHeaders().getFirst(USER_ID_HEADER);
        Long userId;
        AuthorizedUser user;

        if (userIdHeader == null) {
            // 헤더 없으면 인증 실패
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        try {
            userId = Long.parseLong(userIdHeader);
        } catch (NumberFormatException e) {
            exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
            return exchange.getResponse().setComplete();
        }

        try {
            // User Service 호출
            user = userServiceAdapter.fetchUserAuthorizationInfo(userId);
        } catch (Exception e) {
            // FeignClient 호출 실패 (User Service 장애 또는 데이터 문제) 시 UNAUTHORIZED 또는 INTERNAL_SERVER_ERROR 처리
            log.error("User 서비스 호출 실패: {}", e.getMessage());
            exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
            return exchange.getResponse().setComplete();
        }

        HttpMethod method = null;
        try {
            method = exchange.getRequest().getMethod();
            if (method == null) {
                // 메서드 정보가 없으면 잘못된 요청
                exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
                return exchange.getResponse().setComplete();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (!hasPermission(user, method, exchange)) {
            // 권한이 없으면 접근 거부
            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
            return exchange.getResponse().setComplete();
        }

        // 모든 검증 통과 시 다음 필터로 전달
        return chain.filter(exchange);
    }

    /**
     * 권한(Role) 기반 접근 제어 (RBAC)
     *
     * - MASTER: 모든 요청 허용
     * - HUB_MANAGER: GET/POST 허용
     * - DELIVERY_MANAGER: GET만 허용
     * - SUPPLIER_MANAGER: 본인 소유 리소스만 허용 (validateSupplierResource)
     */
    protected boolean hasPermission(AuthorizedUser user, HttpMethod method, ServerWebExchange exchange) {
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
     * URL에서 리소스 ID 추출
     * 예: /orders/{orderId}/status → segments[2] = orderId
     * 각 서비스별 Resource 기반 권한 체크용
     */
    private String extractResourceId(ServerWebExchange exchange) {
        String[] segments = exchange.getRequest().getURI().getPath().split("/");
        return segments.length >= 3 ? segments[2] : null;
    }

    /**
     * SUPPLIER_MANAGER 리소스 소유권 검증
     * - 현재는 항상 true 반환 (임시)
     * - 실제 서비스에서는 DB 조회 등으로 본인 리소스 여부 확인
     */
    protected boolean validateSupplierResource(AuthorizedUser user, String resourceId) {
        log.debug("임시 리소스 검증 (true 반환) userId={} resourceId={}", user.userId(), resourceId);
        return true;
    }
}
