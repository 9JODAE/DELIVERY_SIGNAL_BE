package com.delivery_signal.eureka.client.delivery.presentation.dto.request;

import java.util.List;
import java.util.UUID;
import lombok.Builder;

public record DeliveryCreateRequest(
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

