package com.delivery_signal.eureka.client.delivery.application.dto.event;

import java.util.UUID;

public record DeliveryCreatedRequestEvent(
    UUID orderId,
    UUID deliveryId
) {
}
