package com.delivery_signal.eureka.client.order.application.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class OrderForDeliveryResult {
    private UUID orderId;
    private UUID supplierCompanyId;
    private UUID receiverCompanyId;
    private UUID deliveryId;
    private String requestNote;
    private LocalDateTime createdAt;
}
