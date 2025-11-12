package com.delivery_signal.eureka.client.order.infrastructure.client.product;

import com.delivery_signal.eureka.client.order.domain.vo.product.ProductInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "company-service", url = "${internal.company.url}")
public interface ProductClient {

    /**
     * 특정 상품목록을 조회합니다.
     *
     * 반환할 상품정보 List(List<Dto> 형태로 반환하시면 됩니다.)
     * UUID productId;
     * UUID companyId;
     * String productName;
     * BigDecimal price;
     *
     * @param productIds 상품의 ID리스트
     * @return 상품리스트
     */
    @GetMapping("/open-api/v1/products")
    List<ProductInfo> getProducts(@RequestParam List<UUID> productIds);
}
