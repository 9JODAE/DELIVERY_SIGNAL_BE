package com.delivery_signal.eureka.client.company.domain.service;

import com.delivery_signal.eureka.client.company.domain.entity.Product;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class ProductDomainService {

    public Product createProduct(UUID companyId, String name, BigDecimal price,  Long createdBy) {
        return Product.builder()
                .companyId(companyId)
                .name(name)
                .price(price)
                .build();
    }
}