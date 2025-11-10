package com.delivery_signal.eureka.client.delivery.presentation.dto.request;

import lombok.Builder;

@Builder
public record DeliveryInfoUpdateRequest(
    String address,
    String recipient,
    String recipientSlackId
) {
}
