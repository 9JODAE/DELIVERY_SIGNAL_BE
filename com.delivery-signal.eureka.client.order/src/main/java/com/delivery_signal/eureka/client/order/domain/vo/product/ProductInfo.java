package com.delivery_signal.eureka.client.order.domain.vo.product;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
public class ProductInfo {
    private final UUID productId;
    private final UUID companyId;
    private final String productName;
    private final BigDecimal price;

    public ProductInfo(UUID id, UUID companyId, String name, BigDecimal price) {
        this.productId = id;
        this.companyId = companyId;
        this.productName = name;
        this.price = price;
    }
}
