package com.delivery_signal.eureka.client.order.application.command;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class UpdateOrderCommand {
    private UUID productId;
    private Integer transferQuantity;
    private String requestNote;
}
