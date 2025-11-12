package com.delivery_signal.eureka.client.order.infrastructure.adapter.out;

import com.delivery_signal.eureka.client.order.application.port.out.ProductQueryPort;
import com.delivery_signal.eureka.client.order.domain.vo.product.ProductInfo;
import com.delivery_signal.eureka.client.order.infrastructure.client.company.CompanyClient;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class ProductQueryAdapter implements ProductQueryPort {

    private final CompanyClient companyClient;

    public ProductQueryAdapter(CompanyClient companyClient) {
        this.companyClient = companyClient;
    }

    @Override
    public List<ProductInfo> getProducts(List<UUID> productIds) {
        return companyClient.getProducts(productIds);
    }
}
