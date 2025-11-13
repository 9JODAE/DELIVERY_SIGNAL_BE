package com.delivery_signal.eureka.client.company.infrastructure.adapter.out;

import com.delivery_signal.eureka.client.company.application.port.out.OrderProductQueryPort;
import com.delivery_signal.eureka.client.company.application.result.OrderProductResult;
import com.delivery_signal.eureka.client.company.domain.entity.Product;
import com.delivery_signal.eureka.client.company.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Order 서비스 통신용 Product Adapter
 * Port를 구현하고 실제 Repository를 호출
 */
@Component
@RequiredArgsConstructor
public class OrderProductQueryAdapter implements OrderProductQueryPort {

    private final ProductRepository productRepository;

    @Override
    public List<OrderProductResult> findProductsByIds(List<UUID> productIds) {
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
