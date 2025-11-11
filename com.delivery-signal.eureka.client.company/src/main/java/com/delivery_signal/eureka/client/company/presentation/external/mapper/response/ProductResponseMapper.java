package com.delivery_signal.eureka.client.company.presentation.external.mapper.response;

import com.delivery_signal.eureka.client.company.application.result.*;
import com.delivery_signal.eureka.client.company.presentation.external.dto.response.*;

public class ProductResponseMapper {

    public static ProductCreateResponseDto toCreateResponse(ProductCreateResult result) {
        return ProductCreateResponseDto.builder()
                .id(result.getId())
                .name(result.getName())
                .companyId(result.getCompanyId())
                .hubId(result.getHubId())
                .build();
    }

    public static ProductDetailResponseDto toDetailResponse(ProductDetailResult result) {
        return ProductDetailResponseDto.builder()
                .id(result.getId())
                .name(result.getName())
                .companyId(result.getCompanyId())
                .hubId(result.getHubId())
                .build();
    }

    public static ProductListResponseDto toListResponse(ProductListResult result) {
        return ProductListResponseDto.builder()
                .id(result.getId())
                .name(result.getName())
                .build();
    }

    public static ProductUpdateResponseDto toUpdateResponse(ProductUpdateResult result) {
        return ProductUpdateResponseDto.builder()
                .id(result.getId())
                .name(result.getName())
                .build();
    }

    public static ProductDeleteResponseDto toDeleteResponse(ProductDeleteResult result) {
        return ProductDeleteResponseDto.builder()
                .id(result.getId())
                .deletedAt(result.getDeletedAt())
                .deletedBy(result.getDeletedBy())
                .build();
    }
}
