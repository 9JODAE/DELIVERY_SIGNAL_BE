package com.delivery_signal.eureka.client.delivery.application.dto;

import java.util.UUID;
import lombok.Builder;

@Builder
public record DeliveryQueryResponse(
    UUID deliveryId,
    UUID orderId,
    UUID departureHubId,
    UUID destinationHubId,
    String status,
    String address,
    String recipient,
    String recipientSlackId,
    Long deliveryManagerId
) {
}
