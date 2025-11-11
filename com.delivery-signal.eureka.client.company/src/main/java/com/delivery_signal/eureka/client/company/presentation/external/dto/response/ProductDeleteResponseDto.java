package com.delivery_signal.eureka.client.company.presentation.external.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 상품 삭제 응답 DTO
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDeleteResponseDto {

    @Schema(description = "삭제된 상품 ID")
    private UUID id;

    @Schema(description = "삭제 처리자 ID")
    private Long deletedBy;

    @Schema(description = "삭제 시각")
    private LocalDateTime deletedAt;
}
