package com.delivery_signal.eureka.client.order.presentation.external.mapper.command;

import com.delivery_signal.eureka.client.order.application.command.UpdateOrderCommand;
import com.delivery_signal.eureka.client.order.presentation.external.dto.request.OrderUpdateRequestDto;
import java.util.UUID;

public class OrderUpdateMapper {
    public static UpdateOrderCommand toCommand(UUID orderId, OrderUpdateRequestDto dto, Long userId) {
        return UpdateOrderCommand.builder()
                .userId(userId)
                .orderId(orderId)
                .productId(dto.getProductId())
                .transferQuantity(dto.getTransferQuantity())
                .requestNote(dto.getRequestNote())
                .build();
    }
}
