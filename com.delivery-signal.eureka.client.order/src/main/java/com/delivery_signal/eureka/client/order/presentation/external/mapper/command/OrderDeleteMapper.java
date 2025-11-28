package com.delivery_signal.eureka.client.order.presentation.external.mapper.command;

import com.delivery_signal.eureka.client.order.application.command.OrderDeleteCommand;

import java.util.UUID;

public class OrderDeleteMapper {
    public static OrderDeleteCommand toCommand(UUID orderId, Long userId) {
        return OrderDeleteCommand.builder()
                .orderId(orderId)
                .userId(userId)
                .build();
    }
}
