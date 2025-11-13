package com.delivery_signal.eureka.client.company.presentation.external.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.UUID;

/**
 * 상품 목록 조회 응답 DTO
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductListResponseDto {

    @Schema(description = "상품 ID")
    private UUID id;

    @Schema(description = "상품명")
    private String name;

    @Schema(description = "업체명")
    private String companyName;

    @Schema(description = "상품 관리 허브명")
    private String hubName;
}
