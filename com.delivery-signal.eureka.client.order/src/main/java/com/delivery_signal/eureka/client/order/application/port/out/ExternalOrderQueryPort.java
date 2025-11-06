package com.delivery_signal.eureka.client.order.application.port.out;

import com.delivery_signal.eureka.client.order.presentation.external.dto.response.OrderForDeliveryResponseDto;

import java.util.Optional;
import java.util.UUID;


public interface ExternalOrderQueryPort {
    Optional<OrderForDeliveryResponseDto> findOrderForDeliveryById(UUID orderId);
}
