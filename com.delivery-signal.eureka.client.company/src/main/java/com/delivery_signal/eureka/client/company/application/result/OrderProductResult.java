package com.delivery_signal.eureka.client.company.application.result;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Order 서비스 통신용 상품 정보 Result
 * <p>
 * Order Service에서 내부 API 호출 시 반환되는 상품 정보
 */
@Getter
@Builder
public class OrderProductResult {
    private final UUID productId;
    private final UUID companyId;
    private final String productName;
    private final BigDecimal price;
}
