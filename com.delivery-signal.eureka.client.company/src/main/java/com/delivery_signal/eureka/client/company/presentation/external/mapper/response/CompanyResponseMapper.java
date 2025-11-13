package com.delivery_signal.eureka.client.company.presentation.external.mapper.response;

import com.delivery_signal.eureka.client.company.application.result.*;
import com.delivery_signal.eureka.client.company.presentation.external.dto.response.*;

import static com.delivery_signal.eureka.client.company.common.CompanyTypeConverter.toDto;

public class CompanyResponseMapper {

    public static CompanyCreateResponseDto toCreateResponse(CompanyCreateResult result) {
        return CompanyCreateResponseDto.builder()
                .companyId(result.getCompanyId())
                .companyName(result.getCompanyName())
                .type(toDto(result.getType()))
                .address(result.getAddress())
                .hubId(result.getHubId())
                .createdAt(result.getCreatedAt())
                .build();
    }

    public static CompanyDetailResponseDto toDetailResponse(CompanyDetailResult result) {
        return CompanyDetailResponseDto.builder()
                .companyId(result.getCompanyId())
                .name(result.getCompanyName())
                .type(toDto(result.getType()))
                .address(result.getAddress())
                .hubId(result.getHubId())
                .createdAt(result.getCreatedAt())
                .updatedAt(result.getUpdatedAt())
                .build();
    }

    public static CompanyListResponseDto toListResponse(CompanyListResult result) {
        return CompanyListResponseDto.builder()
                .companyId(result.getCompanyId())
                .companyName(result.getCompanyName())
                .type(toDto(result.getType()))
                .hubId(result.getHubId())
                .build();
    }

    public static CompanyUpdateResponseDto toUpdateResponse(CompanyUpdateResult result) {
        return CompanyUpdateResponseDto.builder()
                .companyId(result.getCompanyId())
                .companyName(result.getCompanyName())
                .type(toDto(result.getType()))
                .address(result.getAddress())
                .updatedAt(result.getUpdatedAt())
                .build();
    }

    public static CompanyDeleteResponseDto toDeleteResponse(CompanyDeleteResult result) {
        return CompanyDeleteResponseDto.builder()
                .companyId(result.getCompanyId())
                .deletedAt(result.getDeletedAt())
                .deletedBy(result.getDeletedBy())
                .build();
    }
}
