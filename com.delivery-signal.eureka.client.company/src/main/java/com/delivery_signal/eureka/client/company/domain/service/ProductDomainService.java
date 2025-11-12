package com.delivery_signal.eureka.client.company.domain.service;

import com.delivery_signal.eureka.client.company.domain.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductDomainService {

    public Product createProduct(String name, BigDecimal price, UUID companyId, UUID hubId, Long createdBy) {
        validatePrice(price);
        validateName(name);

        return Product.builder()
                .productId(UUID.randomUUID())
                .productName(name)
                .price(price)
                .companyId(companyId)
                .hubId(hubId)
                .createdBy(createdBy)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    private void validatePrice(BigDecimal price) {
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException("가격은 0 이상이어야 합니다.");
    }

    private void validateName(String name) {
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("상품명은 필수입니다.");
    }
}
