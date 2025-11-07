package com.delivery_signal.eureka.client.hub.infrastructure.external;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "product-service", path = "/open-api/products")
public interface ProductFeign {

	/**
	 * 상품 존재 여부 확인
	 *
	 * productId에 해당하는 상품이 존재하면 true, 존재하지 않으면 false를 반환한다.
	 *
	 * @param productId 상품 ID
	 * @return 상품 존재여부
	 */
	@GetMapping("/exists")
	boolean exists(@RequestParam("id")UUID productId);
}
