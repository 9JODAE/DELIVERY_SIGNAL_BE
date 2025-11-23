package com.delivery_signal.eureka.client.delivery.presentation.dto.request;

import java.util.UUID;

public record DeliveryDeleteRequest(
    UUID deliveryId,
    Long currUserId
) {
}
