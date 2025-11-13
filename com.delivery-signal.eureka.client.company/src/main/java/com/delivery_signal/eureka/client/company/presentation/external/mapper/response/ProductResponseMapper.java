package com.delivery_signal.eureka.client.company.presentation.external.mapper.response;

import com.delivery_signal.eureka.client.company.application.result.*;
import com.delivery_signal.eureka.client.company.presentation.external.dto.response.*;

public class ProductResponseMapper {

    public static ProductCreateResponseDto toCreateResponse(ProductCreateResult result) {
        return ProductCreateResponseDto.builder()
                .id(result.getProductId())
                .name(result.getProductName())
                .companyId(result.getCompanyId())
                .hubId(result.getHubId())
                .createdAt(result.getCreatedAt())
                .build();
    }

    public static ProductDetailResponseDto toDetailResponse(ProductDetailResult result) {
        return ProductDetailResponseDto.builder()
                .id(result.getProductId())
                .name(result.getProductName())
                .companyId(result.getCompanyId())
                .hubId(result.getHubId())
                .createdAt(result.getCreatedAt())
                .build();
    }

    public static ProductListResponseDto toListResponse(ProductListResult result) {
        return ProductListResponseDto.builder()
                .id(result.getProductId())
                .name(result.getProductName())
                .build();
    }

    public static ProductUpdateResponseDto toUpdateResponse(ProductUpdateResult result) {
        return ProductUpdateResponseDto.builder()
                .id(result.getProductId())
                .updatedAt(result.getUpdateAt())
                .name(result.getProductName())
                .price(result.getPrice())
                .build();
    }

    public static ProductDeleteResponseDto toDeleteResponse(ProductDeleteResult result) {
        return ProductDeleteResponseDto.builder()
                .id(result.getProductId())
                .deletedAt(result.getDeletedAt())
                .deletedBy(result.getDeletedBy())
                .build();
    }
}
