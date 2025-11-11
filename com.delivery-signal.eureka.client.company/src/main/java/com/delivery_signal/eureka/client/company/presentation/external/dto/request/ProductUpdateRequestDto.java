package com.delivery_signal.eureka.client.company.presentation.external.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * 상품 수정 요청 DTO
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductUpdateRequestDto {

    @Schema(description = "상품명", example = "고급 커피콩")
    private String name;

    @Schema(description = "상품 설명", example = "업데이트된 상품 설명입니다.")
    private String description;
}
