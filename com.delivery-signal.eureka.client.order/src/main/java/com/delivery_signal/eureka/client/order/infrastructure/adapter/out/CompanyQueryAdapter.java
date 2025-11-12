package com.delivery_signal.eureka.client.order.infrastructure.adapter.out;

import com.delivery_signal.eureka.client.order.application.port.out.CompanyQueryPort;
import com.delivery_signal.eureka.client.order.domain.vo.company.CompanyInfo;
import com.delivery_signal.eureka.client.order.domain.vo.product.ProductInfo;
import com.delivery_signal.eureka.client.order.infrastructure.client.company.CompanyClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

/**
 * 외부 Company/상품 정보를 조회하는 어댑터
 */
@Component
@RequiredArgsConstructor
public class CompanyQueryAdapter implements CompanyQueryPort {

    private final CompanyClient companyClient;

    @Override
    public CompanyInfo getCompanyById(UUID companyId) {
        return companyClient.getCompanyById(companyId).getData();
    }

    @Override
    public List<ProductInfo> getProducts(List<UUID> productIds) {
        return companyClient.getProducts(productIds).getData();
    }
}

