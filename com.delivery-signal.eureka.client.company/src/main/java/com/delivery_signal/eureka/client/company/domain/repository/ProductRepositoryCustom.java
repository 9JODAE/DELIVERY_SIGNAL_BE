package com.delivery_signal.eureka.client.company.domain.repository;

import com.delivery_signal.eureka.client.company.domain.entity.Product;

import java.util.List;
import java.util.UUID;

public interface ProductRepositoryCustom {
    List<Product> findAllByDeletedAtIsNull();
    List<Product> findByHubIdAndDeletedAtIsNull(UUID hubId);
}
