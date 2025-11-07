package com.delivery_signal.eureka.client.delivery.presentation.dto.response;

import com.delivery_signal.eureka.client.delivery.domain.model.Delivery;
import java.util.UUID;
import lombok.Builder;

@Builder
public record DeliveryCreateResponse(
    UUID deliveryId,
    UUID orderId,
    String status,
    String address,
    String recipient,
    String recipientSlackId,
    Long deliveryManagerId
) {
}
