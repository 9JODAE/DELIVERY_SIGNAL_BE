package com.delivery_signal.eureka.client.order.application.service.external;

import com.delivery_signal.eureka.client.order.domain.vo.ProductInfo;

import java.util.List;
import java.util.UUID;

public interface ProductService {
    List<ProductInfo> getProducts(List<UUID> productIds);
}

