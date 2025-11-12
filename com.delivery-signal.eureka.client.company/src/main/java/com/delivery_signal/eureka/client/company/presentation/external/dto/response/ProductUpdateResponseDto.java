package com.delivery_signal.eureka.client.company.presentation.external.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 상품 수정 응답 DTO
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductUpdateResponseDto {

    @Schema(description = "상품 ID")
    private UUID id;

    @Schema(description = "수정된 상품명")
    private String name;

    @Schema(description = "가격", example = "1000")
    private BigDecimal price;

    @Schema(description = "수정 시각")
    private LocalDateTime updatedAt;
}
