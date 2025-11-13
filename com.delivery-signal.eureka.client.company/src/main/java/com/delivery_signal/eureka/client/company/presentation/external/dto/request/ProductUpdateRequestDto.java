package com.delivery_signal.eureka.client.company.presentation.external.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;

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

    @Schema(description = "가격", example = "1000")
    private BigDecimal price;

}
