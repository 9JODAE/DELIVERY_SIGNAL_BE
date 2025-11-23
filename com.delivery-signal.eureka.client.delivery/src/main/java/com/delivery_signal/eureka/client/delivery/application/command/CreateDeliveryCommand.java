package com.delivery_signal.eureka.client.delivery.application.command;

import java.util.UUID;
import lombok.Builder;

@Builder
public record CreateDeliveryCommand(
    Long userId,
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
