package com.delivery_signal.eureka.client.company.application.result;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class ProductUpdateResult {
    private UUID productId;
    private String productName;
    private Long updatedBy;
    private LocalDateTime updateAt;
    private BigDecimal price;
    private String message;
}
