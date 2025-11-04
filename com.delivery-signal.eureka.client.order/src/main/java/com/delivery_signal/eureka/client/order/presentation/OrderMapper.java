package com.delivery_signal.eureka.client.order.presentation;

import com.delivery_signal.eureka.client.order.application.command.CreateOrderCommand;
import com.delivery_signal.eureka.client.order.application.command.OrderProductCommand;
import com.delivery_signal.eureka.client.order.presentation.dto.request.CreateOrderRequestDto;

import java.util.List;

public class OrderMapper {
    public static CreateOrderCommand toCommand(CreateOrderRequestDto dto) {
        List<OrderProductCommand> products = dto.getOrderProducts().stream()
                .map(p -> OrderProductCommand.builder()
                        .productId(p.getProductId())
                        .quantity(p.getQuantity())
                        .build())
                .toList();

        return CreateOrderCommand.builder()
                .supplierCompanyId(dto.getSupplierCompanyId())
                .receiverCompanyId(dto.getReceiverCompanyId())
                .requestNote(dto.getRequestNote())
                .products(products)
                .build();
    }
}
