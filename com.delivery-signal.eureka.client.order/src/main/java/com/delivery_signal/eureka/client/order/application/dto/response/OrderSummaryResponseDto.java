package com.delivery_signal.eureka.client.order.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class OrderSummaryResponseDto {
    private UUID productId;
    private UUID orderId;
    private String productName;
    private BigDecimal productPriceAtOrder;
    private int quantity;

}
