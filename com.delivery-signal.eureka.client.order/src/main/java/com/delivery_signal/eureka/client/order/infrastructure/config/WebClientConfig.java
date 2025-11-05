package com.delivery_signal.eureka.client.order.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * MSA 에서 게이트웨이로 인증인가를 하기 위해 WebClient 사용.
 * 동기 통신에 사용예정, 비동기 통신은 다른 거 추가할 필요 있음.
 */
@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        return builder
                .baseUrl("http://gateway-service") // 게이트웨이 주소
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
