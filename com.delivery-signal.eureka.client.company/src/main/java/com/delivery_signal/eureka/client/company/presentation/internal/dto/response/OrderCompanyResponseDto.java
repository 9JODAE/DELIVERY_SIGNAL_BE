package com.delivery_signal.eureka.client.company.presentation.internal.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

/**
 * Order 서비스 통신용 업체 정보 DTO
 */
@Getter
@Builder
public class OrderCompanyResponseDto {

    @Schema(description = "업체 ID", example = "c8b9e0fa-7e63-4cb0-bf2c-87b58d8b0e91")
    private UUID companyId;

    @Schema(description = "소속 허브 ID", example = "5b5e1e34-9e9f-4f43-94fa-3b93b05d3f3c")
    private UUID hubId;

    @Schema(description = "업체 주소", example = "서울특별시 강남구 테헤란로 123")
    private String address;
}

