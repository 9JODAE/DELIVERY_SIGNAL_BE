package com.delivery_signal.eureka.client.order.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * MSA 에서 게이트웨이로 인증인가를 하기 위해 WebClient 사용.
 * 동기 통신에 사용예정, 비동기 통신은 다른 거 추가할 필요 있음.
 */
@Configuration
public class WebClientConfig {

    @Value("${gateway.base-url}")
    private String gatewayBaseUrl;

    /**
     * 게이트웨이를 통한 외부/내부 통신용 WebClient
     * 모든 요청이 게이트웨이(19091 or 80)로 전달됨
     */
    @Bean
    public WebClient gatewayWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl(gatewayBaseUrl)
                .build();
    }

    /**
     * Eureka 이름 기반 직접 통신용 WebClient.Builder
     * lb://{service-name} 형태로 직접 호출 가능
     * 필요할 때만 baseUrl 지정해서 사용
     */
    @Bean
    @LoadBalanced
    public WebClient.Builder loadBalancedWebClientBuilder() {
        return WebClient.builder();
    }
}


