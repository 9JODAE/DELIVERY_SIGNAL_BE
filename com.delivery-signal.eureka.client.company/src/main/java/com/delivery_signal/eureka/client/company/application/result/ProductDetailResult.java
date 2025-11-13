package com.delivery_signal.eureka.client.company.application.result;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
public class ProductDetailResult {
    private UUID productId;
    private UUID companyId;
    private UUID hubId;
    private String productName;
    private BigDecimal price;
}
