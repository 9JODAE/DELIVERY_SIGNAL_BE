package com.delivery_signal.eureka.client.company.application.command;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
public class UpdateProductCommand {
    private Long userId;
    private UUID productId;
    private String name;
    private Integer quantity;
    private BigDecimal price;
}