package com.delivery_signal.eureka.client.order.application.port.out;

import com.delivery_signal.eureka.client.order.domain.vo.company.CompanyInfo;
import com.delivery_signal.eureka.client.order.domain.vo.product.ProductInfo;

import java.util.List;
import java.util.UUID;

/**
 * 업체 컨텍스트에서 쓸 메서드 port
 */
public interface CompanyQueryPort {
    CompanyInfo getCompanyById(UUID companyId);
    List<ProductInfo> getProducts(List<UUID> productIds);
}

