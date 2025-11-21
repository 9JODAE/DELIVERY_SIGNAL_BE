package com.delivery_signal.eureka.client.order.application.result;

import com.delivery_signal.eureka.client.order.domain.vo.delivery.DeliveryStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderForDeliveryResult {
    private UUID orderId;
    private UUID supplierCompanyId;
    private UUID receiverCompanyId;
    private UUID deliveryId;
    private String requestNote;
    private DeliveryStatus status;
    private LocalDateTime createdAt;
}
