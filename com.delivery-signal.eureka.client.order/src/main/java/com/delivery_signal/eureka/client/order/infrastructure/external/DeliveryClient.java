package com.delivery_signal.eureka.client.order.infrastructure.external;

import com.delivery_signal.eureka.client.order.presentation.external.dto.request.DeliveryCreateRequest;
import com.delivery_signal.eureka.client.order.presentation.external.dto.response.DeliveryCreateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class DeliveryClient {
    private final WebClient webClient;

    public DeliveryCreateResponse createDelivery(
            String jwtToken, DeliveryCreateRequest deliveryCreateRequest )
    {
        return webClient
                .post()
                .uri("/delivery-service/deliveries")
                .header("Authorization", "Bearer " + jwtToken)
                .bodyValue(deliveryCreateRequest) .retrieve()
                .bodyToMono(DeliveryCreateResponse.class)
                .block();
         }
}