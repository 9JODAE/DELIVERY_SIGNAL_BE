package com.delivery_signal.eureka.client.company.presentation.internal.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Order 서비스 통신용 상품 정보 DTO
 */
@Getter
@Builder
public class OrderProductResponseDto {

    @Schema(description = "상품 ID", example = "e42f3ad5-99d4-4b31-bf40-134ad8f14a21")
    private UUID productId;

    @Schema(description = "소속 업체 ID", example = "a98e50c5-b0d0-4a2f-a4fa-b8df9e21f233")
    private UUID companyId;

    @Schema(description = "상품명", example = "프리미엄 도시락 세트")
    private String productName;

    @Schema(description = "상품 가격", example = "12900")
    private BigDecimal price;
}
