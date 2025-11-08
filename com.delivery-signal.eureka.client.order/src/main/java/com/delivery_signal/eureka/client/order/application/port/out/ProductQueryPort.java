package com.delivery_signal.eureka.client.order.application.port.out;

import com.delivery_signal.eureka.client.order.domain.vo.product.ProductInfo;

import java.util.List;
import java.util.UUID;

public interface ProductQueryPort {
    List<ProductInfo> getProducts(List<UUID> productIds);
}

