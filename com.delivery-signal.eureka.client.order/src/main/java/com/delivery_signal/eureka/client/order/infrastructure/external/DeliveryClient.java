package com.delivery_signal.eureka.client.order.infrastructure.external;

import com.delivery_signal.eureka.client.order.presentation.external.dto.request.DeliveryCreateRequest;
import com.delivery_signal.eureka.client.order.presentation.external.dto.response.DeliveryCreateResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class DeliveryClient {

    private final WebClient webClient;

    public DeliveryCreateResponseDto createDelivery(
            String jwtToken, DeliveryCreateRequest deliveryCreateRequest) {

        return webClient
                .post()
                .uri("/api/v1/deliveries") // 게이트웨이 라우팅 경로에 맞춤
                .header("Authorization", "Bearer " + jwtToken)
                .bodyValue(deliveryCreateRequest)
                .retrieve()
                .bodyToMono(DeliveryCreateResponseDto.class)
                .block();
    }
}
