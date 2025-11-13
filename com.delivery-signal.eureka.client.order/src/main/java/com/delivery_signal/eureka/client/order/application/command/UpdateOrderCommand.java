package com.delivery_signal.eureka.client.order.application.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateOrderCommand {
    private UUID orderId;
    private UUID productId;
    private Long userId;
    private Integer transferQuantity;
    private String requestNote;
}
