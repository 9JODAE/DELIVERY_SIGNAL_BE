package com.delivery_signal.eureka.client.order.application.command;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderUpdateCommand {
    private UUID orderId;
    private UUID productId;
    private Long userId;
    private Integer transferQuantity;
    private String requestNote;
}
