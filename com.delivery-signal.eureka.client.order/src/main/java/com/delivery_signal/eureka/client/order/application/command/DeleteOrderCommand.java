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
public class DeleteOrderCommand {
    private UUID orderId;
    private Long userId;
}
