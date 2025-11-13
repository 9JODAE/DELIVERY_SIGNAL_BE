package com.delivery_signal.eureka.client.order.application.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderListResult {
    private UUID orderId;
    private UUID supplierCompanyId;
    private UUID receiverCompanyId;
    private UUID deliveryId;
    private List<OrderProductResult> products;
    private BigDecimal totalPrice;
    private String requestNote;
    private LocalDateTime createdAt;
    private Long createdBy;
    private LocalDateTime updatedAt;
    private Long updatedBy;
    private LocalDateTime deletedAt;
    private Long deletedBy;
}
