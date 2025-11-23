package com.delivery_signal.eureka.client.delivery.infrastructure.dto;

import java.util.UUID;
import lombok.Builder;

@Builder
public record DeliveryCreateDto(
    Long requestUserId,
    UUID orderId,
    UUID companyId,
    String status,
    UUID departureHubId,
    UUID destinationHubId,
    String address,
    String recipient,
    String recipientSlackId
) {
}
