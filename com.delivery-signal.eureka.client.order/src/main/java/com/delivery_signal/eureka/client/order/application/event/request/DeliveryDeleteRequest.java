package com.delivery_signal.eureka.client.order.application.event.request;

import java.util.UUID;

public record DeliveryDeleteRequest(
    UUID deliveryId,
    Long currUserId
) {
}