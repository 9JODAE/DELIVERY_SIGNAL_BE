package com.delivery_signal.eureka.client.order.presentation.external.mapper.response;

import com.delivery_signal.eureka.client.order.application.result.OrderProductResult;
import com.delivery_signal.eureka.client.order.presentation.external.dto.response.OrderProductResponseDto;

public class OrderProductResponseMapper {

    public static OrderProductResponseDto toResponseDto(OrderProductResult result) {
        return OrderProductResponseDto.builder()
                .productId(result.getProductId())
                .quantity(result.getQuantity())
                .build();
    }
}

