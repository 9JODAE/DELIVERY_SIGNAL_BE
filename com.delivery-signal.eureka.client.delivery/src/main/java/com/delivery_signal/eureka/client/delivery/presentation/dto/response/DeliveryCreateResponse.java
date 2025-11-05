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
    public static DeliveryCreateResponse from(Delivery delivery) {
        return DeliveryCreateResponse.builder()
            .deliveryId(delivery.getDeliveryId())
            .orderId(delivery.getOrderId())
            .status(delivery.getCurrStatus().getDescription())
            .address(delivery.getDeliveryAddress())
            .recipient(delivery.getRecipient())
            .recipientSlackId(delivery.getRecipientSlackId())
            .deliveryManagerId(delivery.getDeliveryManagerId())
            .build();
    }
}
