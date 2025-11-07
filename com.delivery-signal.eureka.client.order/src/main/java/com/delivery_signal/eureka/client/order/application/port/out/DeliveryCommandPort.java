package com.delivery_signal.eureka.client.order.application.port.out;

import com.delivery_signal.eureka.client.order.application.dto.request.CreateDeliveryRequestDto;
import com.delivery_signal.eureka.client.order.domain.vo.delivery.DeliveryCreatedInfo;
import com.delivery_signal.eureka.client.order.presentation.internal.dto.response.OrderForDeliveryResponseDto;

import java.util.Optional;
import java.util.UUID;

public interface DeliveryCommandPort {
    Optional<OrderForDeliveryResponseDto> findOrderForDeliveryById(UUID orderId);

    DeliveryCreatedInfo createDelivery(CreateDeliveryRequestDto deliveryRequest);

}
