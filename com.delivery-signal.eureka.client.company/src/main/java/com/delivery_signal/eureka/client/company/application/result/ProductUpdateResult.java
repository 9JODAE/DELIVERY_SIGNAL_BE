package com.delivery_signal.eureka.client.company.application.result;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
public class ProductUpdateResult {
    private UUID id;
    private String name;
    private Integer quantity;
    private BigDecimal price;
}
