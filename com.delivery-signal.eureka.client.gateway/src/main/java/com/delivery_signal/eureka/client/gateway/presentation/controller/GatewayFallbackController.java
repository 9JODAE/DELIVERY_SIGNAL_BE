package com.delivery_signal.eureka.client.gateway.presentation.controller;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.CIRCUITBREAKER_EXECUTION_EXCEPTION_ATTR;

import com.delivery_signal.eureka.client.gateway.presentation.dto.ApiResponse;
import com.delivery_signal.eureka.client.gateway.presentation.dto.FallbackResponse;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.RouteMatcher.Route;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestController
public class GatewayFallbackController {

    @RequestMapping("/delivery-fallback")
    public Mono<ResponseEntity<String>> deliveryServiceFallback() {
        // HTTP 503 Service Unavailable 상태와 함께 에러 메시지 비동기적으로 반환
        return Mono.just(
            ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE) // 상태 코드 503
                .body("{\"message\": \"현재 배송 서비스가 불안정합니다. 잠시 후 다시 시도해 주세요.\","
                    + "\"errorCode\": 503}")
        );
    }

    /**
     * 고급 설정 (추후 개선 예정)
     */
//    public ResponseEntity<Mono<ApiResponse<FallbackResponse>>> fallback(ServerWebExchange exchange) {
//        Route route = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
//        Exception exception = exchange.getAttribute(CIRCUITBREAKER_EXECUTION_EXCEPTION_ATTR);
//
//        String routeValue = route != null ? route.value() : "unknown";
//        String errorMessage = exception != null ? exception.getMessage() : "No error info";
//
//        FallbackResponse response = FallbackResponse.builder()
//            .statusCode(503)
//            .error(routeValue + " : " + errorMessage)
//            .message("서비스 중단")
//            .build();
//
//        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(Mono.just(ApiResponse.error(response)));
//    }
}
