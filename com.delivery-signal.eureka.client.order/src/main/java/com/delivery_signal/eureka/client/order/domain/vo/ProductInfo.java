package com.delivery_signal.eureka.client.order.domain.vo;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
public class ProductInfo {
    private final UUID productId;
    private final String productName;
    private final BigDecimal price;

    public ProductInfo(UUID id, String name, BigDecimal price) {
        this.productId = id;
        this.productName = name;
        this.price = price;
    }
}
