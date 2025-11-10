package com.delivery_signal.eureka.client.order.infrastructure.client.product;

import com.delivery_signal.eureka.client.order.domain.vo.product.ProductInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "product-service", url = "${internal.product.url}")
public interface ProductClient {

    /**
     * 특정 업체에 속한 상품 정보 리스트를 조회합니다.
     *
     * 요청한 companyId에 속하는 모든 상품 정보를 List형태로 반환해주세요.
     * 반환해야할 상품리스트의 개별 정보는 다음과 같습니다.
     *  UUID productId;
     *  UUID companyId;
     *  String productName;
     *  BigDecimal price;
     *
     * @param companyId 조회할 업체 ID
     * @return 해당 업체에 속한 모든 상품 정보 리스트
     */
    @GetMapping("open-api//products")
    List<ProductInfo> getProductsByCompany(@RequestParam UUID companyId);


    /**
     * 특정 상품을 조회합니다.
     * @param id
     * @return
     */
    @GetMapping("open-api//products/{id}")
    ProductInfo getProduct(@PathVariable UUID id);

    @GetMapping("open-api//products")
    List<ProductInfo> getProducts(@RequestParam List<UUID> productIds);
}
