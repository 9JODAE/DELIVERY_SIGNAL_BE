package com.delivery_signal.eureka.client.order.infrastructure.external.product;

import com.delivery_signal.eureka.client.order.application.service.external.ProductService;
import com.delivery_signal.eureka.client.order.domain.vo.ProductInfo;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

// Infrastructure Layer
@Component
public class ProductClientImpl implements ProductService {

    private final ProductClient productClient; // 실제 외부 API 클라이언트

    public ProductClientImpl(ProductClient productClient) {
        this.productClient = productClient;
    }

    @Override
    public List<ProductInfo> getProducts(List<UUID> productIds) {
        return productClient.getProducts(productIds);
    }
}

