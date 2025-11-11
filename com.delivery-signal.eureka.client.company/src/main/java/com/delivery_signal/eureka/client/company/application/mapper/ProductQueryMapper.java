package com.delivery_signal.eureka.client.company.application.mapper;

import com.delivery_signal.eureka.client.company.application.result.ProductDetailResult;
import com.delivery_signal.eureka.client.company.application.result.ProductListResult;
import com.delivery_signal.eureka.client.company.domain.entity.Product;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 상품 조회 전용 매퍼
 * - Entity → Result 변환 담당
 */
@Component
public class ProductQueryMapper {

    /** 단건 리스트용 매핑 */
    public ProductListResult toListDto(Product product) {
        return ProductListResult.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .build();
    }

    /** 전체 리스트 매핑 */
    public List<ProductListResult> toListDtos(List<Product> products) {
        return products.stream()
                .map(this::toListDto)
                .toList();
    }

    /** 상세 조회용 매핑 */
    public ProductDetailResult toDetailDto(Product product) {
        return ProductDetailResult.builder()
                .id(product.getId())
                .name(product.getName())
                .companyId(product.getCompanyId())
                .hubId(product.getHubId())
                .build();
    }
}
