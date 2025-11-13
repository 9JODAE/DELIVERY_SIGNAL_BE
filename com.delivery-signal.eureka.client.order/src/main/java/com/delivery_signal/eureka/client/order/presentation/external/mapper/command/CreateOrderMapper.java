package com.delivery_signal.eureka.client.order.presentation.external.mapper.command;

import com.delivery_signal.eureka.client.order.application.command.CreateOrderCommand;
import com.delivery_signal.eureka.client.order.application.command.OrderProductCommand;
import com.delivery_signal.eureka.client.order.presentation.external.dto.request.CreateOrderRequestDto;

import java.util.List;

public class CreateOrderMapper {

    public static CreateOrderCommand toCommand(CreateOrderRequestDto dto, Long userId) {
        List<OrderProductCommand> products = dto.getOrderProducts().stream()
                .map(p -> OrderProductCommand.builder()
                        .productId(p.getProductId())
                        .quantity(p.getQuantity())
                        .build())
                .toList();

        return CreateOrderCommand.builder()
                .userId(userId)
                .supplierCompanyId(dto.getSupplierCompanyId())
                .receiverCompanyId(dto.getReceiverCompanyId())
                .recipient(dto.getRecipient())
                .recipientSlackId(dto.getRecipientSlackId())
                .requestNote(dto.getRequestNote())
                .products(products)
                .build();
    }
}
