package com.delivery_signal.eureka.client.order.application.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderCancelResult {
    private UUID orderId;
    private UUID deliveryId;
    private String orderStatus;
    private String message;
}
