package com.delivery_signal.eureka.client.company.application.service;

import com.delivery_signal.eureka.client.company.application.result.OrderProductResult;
import com.delivery_signal.eureka.client.company.domain.entity.Product;
import com.delivery_signal.eureka.client.company.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Order 서비스 통신용 Internal Product Service
 */
@Service
@RequiredArgsConstructor
public class InternalOrderProductService {

    private final ProductRepository productRepository;

    public List<OrderProductResult> getProducts(List<UUID> productIds) {
        List<Product> products = productRepository.findAllById(productIds);

        return products.stream()
                .map(p -> OrderProductResult.builder()
                        .productId(p.getProductId())
                        .companyId(p.getCompanyId())
                        .productName(p.getProductName())
                        .price(p.getPrice())
                        .build())
                .collect(Collectors.toList());
    }
}

