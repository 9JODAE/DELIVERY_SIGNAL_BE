package com.delivery_signal.eureka.client.order.presentation.dto.response;

import com.delivery_signal.eureka.client.order.application.dto.response.OrderQueryResponseDto;
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
public class OrderListResponseDto {
    UUID orderId;
    UUID supplierCompanyId;
    UUID receiverCompanyId;
    UUID deliveryId;
    List<OrderQueryResponseDto> products;
    BigDecimal totalPrice;
    String requestNote;
    LocalDateTime createdAt;
    UUID createdBy;
    LocalDateTime updatedAt;
    UUID updatedBy;
    LocalDateTime deletedAt;
    UUID deletedBy;
}
