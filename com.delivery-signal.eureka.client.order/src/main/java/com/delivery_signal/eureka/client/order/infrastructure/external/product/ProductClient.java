package com.delivery_signal.eureka.client.order.infrastructure.external.product;

import com.delivery_signal.eureka.client.order.domain.vo.ProductInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "product-service", url = "${external.product.url}")
public interface ProductClient {

    @GetMapping("/products/{id}")
    ProductInfo getProduct(@PathVariable UUID id);

    @GetMapping("/products")
    List<ProductInfo> getProducts(@RequestParam List<UUID> productIds);
}
