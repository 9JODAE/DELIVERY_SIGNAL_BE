package com.delivery_signal.eureka.client.order.presentation.external.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class OrderProductResponseDto {
    private UUID productId;
    private String productName;
    private Integer quantity;
    private BigDecimal price;
}

