package com.delivery_signal.eureka.client.order.presentation.external.mapper.command;

import com.delivery_signal.eureka.client.order.application.command.OrderCancelCommand;

import java.util.UUID;

public class OrderCancelMapper {
    public static OrderCancelCommand toCommand(UUID orderId, Long userId) {
        return OrderCancelCommand.builder()
                .orderId(orderId)
                .userId(userId)
                .build();
    }
}