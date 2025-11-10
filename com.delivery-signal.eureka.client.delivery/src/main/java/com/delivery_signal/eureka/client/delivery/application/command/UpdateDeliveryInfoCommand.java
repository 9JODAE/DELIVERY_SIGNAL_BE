package com.delivery_signal.eureka.client.delivery.application.command;

import lombok.Builder;

@Builder
public record UpdateDeliveryInfoCommand(
    String address,
    String recipient,
    String recipientSlackId
) {
}
