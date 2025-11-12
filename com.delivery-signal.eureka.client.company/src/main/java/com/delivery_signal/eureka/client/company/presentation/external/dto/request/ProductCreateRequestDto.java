package com.delivery_signal.eureka.client.company.presentation.external.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * 상품 생성 요청 DTO
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductCreateRequestDto {

    @Schema(description = "상품명", example = "고급 커피콩")
    private String name;

    @Schema(description = "상품 소속 업체 ID", example = "1")
    private UUID companyId;

    @Schema(description = "상품 관리 허브 ID", example = "d5a2b3f7-1234-4a56-9abc-ef7890d12345")
    private UUID hubId;

    @Schema(description = "상품 설명", example = "브라질산 아라비카 원두 1kg")
    private String description;

    private BigDecimal price;
}


