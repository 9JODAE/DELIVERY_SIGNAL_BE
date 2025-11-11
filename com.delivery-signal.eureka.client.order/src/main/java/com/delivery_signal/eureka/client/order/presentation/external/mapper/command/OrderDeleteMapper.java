package com.delivery_signal.eureka.client.order.presentation.external.mapper.command;

import com.delivery_signal.eureka.client.order.application.command.DeleteOrderCommand;
import java.util.UUID;

public class OrderDeleteMapper {
    public static DeleteOrderCommand toCommand(UUID orderId) {
        return DeleteOrderCommand.builder()
                .orderId(orderId)
                .build();
    }
}
