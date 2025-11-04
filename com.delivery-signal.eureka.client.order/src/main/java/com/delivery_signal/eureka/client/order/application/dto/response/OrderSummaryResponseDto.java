package com.delivery_signal.eureka.client.order.application.dto.response;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;


@Getter
public class OrderSummaryResponseDto {

    private UUID productId;
    private UUID orderId;
    private String productName;
    private BigDecimal productPriceAtOrder;
    private int quantity;

    public OrderSummaryResponseDto() {
        // Jackson 역직렬화용
    }

    public OrderSummaryResponseDto(UUID productId, UUID orderId, String productName,BigDecimal productPrice ,int quantity) {
        this.productId = productId;
        this.orderId = orderId;
        this.productPriceAtOrder = productPrice;
        this.productName = productName;
        this.quantity = quantity;
    }
}
