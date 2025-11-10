package com.delivery_signal.eureka.client.order.infrastructure.filter;

import com.delivery_signal.eureka.client.order.application.port.out.UserQueryPort;
import com.delivery_signal.eureka.client.order.domain.vo.user.UserAuthorizationInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@Order(2) // 필터 순서: AuthenticationFilter(1) 이후 실행
public class ResourceAuthorizationFilter implements WebFilter {

    // 클라이언트에서 전달되는 유저 ID 헤더 이름
    private static final String USER_ID_HEADER = "x-user-id";

    // 사용자 정보 조회를 위한 Port (Domain Layer 의존)
    private final UserQueryPort userQueryPort;

    public ResourceAuthorizationFilter(UserQueryPort userQueryPort) {
        this.userQueryPort = userQueryPort;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        // 🔹 내부 호출 확인
        // /open-api/로 시작하는 URL은 서비스 내부 호출로 간주, 권한 체크 생략
        String path = exchange.getRequest().getURI().getPath();
        if (path.startsWith("/open-api/")) {
            return chain.filter(exchange);
        }

        // 🔹 외부 호출: x-user-id 헤더 확인
        String userIdHeader = exchange.getRequest().getHeaders().getFirst(USER_ID_HEADER);
        if (userIdHeader == null) {
            // 헤더 없으면 인증 실패
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        // 🔹 userId 형식 검증
        Long userId;
        try {
            userId = Long.parseLong(userIdHeader);
        } catch (NumberFormatException e) {
            // 숫자 변환 실패 -> 잘못된 요청
            exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
            return exchange.getResponse().setComplete();
        }

        // 🔹 사용자 정보 조회 (User 서비스 호출)
        UserAuthorizationInfo userInfo;
        try {
            userInfo = userQueryPort.getUserAuthorizationInfo(userId);
        } catch (Exception e) {
            // 외부 서비스 호출 실패 시 서비스 불가 상태 반환
            log.error("User 서비스 호출 실패: {}", e.getMessage());
            exchange.getResponse().setStatusCode(HttpStatus.SERVICE_UNAVAILABLE);
            return exchange.getResponse().setComplete();
        }

        // 🔹 사용자 활성 여부 확인
        if (userInfo == null || !userInfo.isActive()) {
            // 비활성 사용자이면 인증 실패
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        // 🔹 HTTP 메서드 확인
        HttpMethod method = exchange.getRequest().getMethod();
        if (method == null) {
            // 메서드 정보가 없으면 잘못된 요청
            exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
            return exchange.getResponse().setComplete();
        }

        // 🔹 권한(Role) 체크
        if (!hasPermission(userInfo, method, exchange)) {
            // 권한이 없으면 접근 거부
            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
            return exchange.getResponse().setComplete();
        }

        // 🔹 모든 검증 통과 시 다음 필터로 전달
        return chain.filter(exchange);
    }

    /**
     * 🔹 URL에서 리소스 ID 추출
     * 예: /orders/{orderId}/status → segments[2] = orderId
     * 각 서비스별 Resource 기반 권한 체크용
     */
    private String extractResourceId(ServerWebExchange exchange) {
        String[] segments = exchange.getRequest().getURI().getPath().split("/");
        return segments.length >= 3 ? segments[2] : null;
    }

    /**
     * 🔹 권한(Role) 기반 접근 제어
     *
     * - MASTER: 모든 요청 허용
     * - HUB_MANAGER: GET/POST 허용
     * - DELIVERY_MANAGER: GET만 허용
     * - SUPPLIER_MANAGER: 본인 소유 리소스만 허용 (validateSupplierResource)
     */
    protected boolean hasPermission(UserAuthorizationInfo user, HttpMethod method, ServerWebExchange exchange) {
        String role = user.getRole();
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
     * - 현재는 항상 true 반환 (임시)
     * - 실제 서비스에서는 DB 조회 등으로 본인 리소스 여부 확인
     */
    protected boolean validateSupplierResource(UserAuthorizationInfo user, String resourceId) {
        log.debug("임시 리소스 검증 (true 반환) userId={} resourceId={}", user.getUserId(), resourceId);
        return true;
    }
}
