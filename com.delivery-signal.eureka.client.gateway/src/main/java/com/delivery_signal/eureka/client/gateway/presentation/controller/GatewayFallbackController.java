package com.delivery_signal.eureka.client.gateway.presentation.controller;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.CIRCUITBREAKER_EXECUTION_EXCEPTION_ATTR;

import com.delivery_signal.eureka.client.gateway.presentation.dto.ApiResponse;
import com.delivery_signal.eureka.client.gateway.presentation.dto.FallbackResponse;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestController
public class GatewayFallbackController {

    public static final String CIRCUITBREAKER_EXECUTION_EXCEPTION_ATTR = "CircuitBreakerExecutionException";

    @RequestMapping("/delivery-fallback")
    public ResponseEntity<Mono<ApiResponse<FallbackResponse>>> fallback(ServerWebExchange exchange) {
        Route route = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
        Exception exception = exchange.getAttribute(CIRCUITBREAKER_EXECUTION_EXCEPTION_ATTR);

        String routeId = route != null ? route.getId() : "unknown-route";
        String errorMessage = exception != null ? exception.getMessage() : "No error info";

        FallbackResponse response = FallbackResponse.builder()
            .statusCode(503)
            .error(routeId + " : " + errorMessage)
            .message("서비스 중단")
            .build();

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(Mono.just(ApiResponse.error(response)));
    }
}
