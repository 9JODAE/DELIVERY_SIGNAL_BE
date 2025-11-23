package com.delivery_signal.eureka.client.order.application.event.response;

import java.util.UUID;

public record DeliveryCreatedResponseEvent(
    UUID orderId,
    UUID deliveryId
) {}