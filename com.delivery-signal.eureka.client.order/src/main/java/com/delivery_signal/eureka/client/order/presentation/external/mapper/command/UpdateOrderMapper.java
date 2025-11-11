package com.delivery_signal.eureka.client.order.presentation.external.mapper.command;

import com.delivery_signal.eureka.client.order.application.command.UpdateOrderCommand;
import com.delivery_signal.eureka.client.order.presentation.external.dto.request.UpdateOrderRequestDto;
import java.util.UUID;

public class UpdateOrderMapper {
    public static UpdateOrderCommand toCommand(UUID orderId, UpdateOrderRequestDto dto) {
        return UpdateOrderCommand.builder()
                .productId(dto.getProductId())
                .transferQuantity(dto.getTransferQuantity())
                .requestNote(dto.getRequestNote())
                .build();
    }
}
