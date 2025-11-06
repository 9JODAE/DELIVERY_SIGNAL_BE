package com.delivery_signal.eureka.client.order.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class OrderDetailResponseDto {
    UUID orderId;
    UUID supplierCompanyId;
    UUID receiverCompanyId;
    UUID deliveryId;
    List<OrderQueryResponseDto> products;
    BigDecimal totalPrice;
    String requestNote;
    LocalDateTime createdAt;
    Long createdBy;
    LocalDateTime updatedAt;
    Long updatedBy;
}
