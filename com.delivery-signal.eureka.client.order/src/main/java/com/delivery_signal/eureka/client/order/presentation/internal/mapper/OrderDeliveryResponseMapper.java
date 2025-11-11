package com.delivery_signal.eureka.client.order.presentation.internal.mapper;

import com.delivery_signal.eureka.client.order.application.result.OrderForDeliveryResult;
import com.delivery_signal.eureka.client.order.presentation.internal.dto.response.DeliveryCreateResponseDto;

public class OrderDeliveryResponseMapper {

    private OrderDeliveryResponseMapper() {
    }

    public static DeliveryCreateResponseDto toResponse(OrderForDeliveryResult result) {
        return DeliveryCreateResponseDto.builder()
                .deliveryId(result.getDeliveryId())
                .orderId(result.getOrderId())
                .createdAt(result.getCreatedAt())
                .status("허브 대기중")
                .build();
    }
}
