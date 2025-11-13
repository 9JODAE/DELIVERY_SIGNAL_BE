//package com.delivery_signal.eureka.client.delivery.infrastructure.filter;
//
//import org.springframework.core.annotation.Order;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//import org.springframework.web.server.WebFilter;
//import org.springframework.web.server.WebFilterChain;
//import reactor.core.publisher.Mono;
//
///**
// * AuthenticationFilter
// *
// * - 목적: 모든 요청에 대해 "로그인 여부"만 확인 (x-user-id 헤더 존재 확인)
// * - 특징: 매우 가벼운 체크만 수행.
// * - 실제 권한(ROLE) 검증은 ResourceAuthorizationFilter에서 처리.
// */
//@Component
//@Order(1)
//public class AuthenticationFilter implements WebFilter {
//
//    private static final String USER_ID_HEADER = "x-user-id";
//
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
//        String userId = exchange.getRequest().getHeaders().getFirst(USER_ID_HEADER);
//
//        if (userId == null) {
//            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
//            return exchange.getResponse().setComplete();
//        }
//
//        return chain.filter(exchange);
//    }
//}
