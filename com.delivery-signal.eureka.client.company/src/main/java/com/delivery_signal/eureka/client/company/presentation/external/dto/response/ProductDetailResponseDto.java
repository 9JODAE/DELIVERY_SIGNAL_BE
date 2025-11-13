package com.delivery_signal.eureka.client.company.presentation.external.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 상품 단건 상세 응답 DTO
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDetailResponseDto {

    @Schema(description = "상품 ID")
    private UUID id;

    @Schema(description = "상품명")
    private String name;

    @Schema(description = "상품 소속 업체 ID")
    private UUID companyId;

    @Schema(description = "상품 관리 허브 ID")
    private UUID hubId;

    @Schema(description = "생성 시각")
    private LocalDateTime createdAt;

}
