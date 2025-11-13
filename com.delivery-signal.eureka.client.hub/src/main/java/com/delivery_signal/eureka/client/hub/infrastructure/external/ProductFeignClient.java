package com.delivery_signal.eureka.client.hub.infrastructure.external;

import java.util.Map;
import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.delivery_signal.eureka.client.hub.common.api.ApiResponse;
import com.delivery_signal.eureka.client.hub.infrastructure.external.dto.ProductDTO;

@FeignClient(name = "product-service", path = "/open-api/v1/products")
public interface ProductFeignClient {

	/**
	 * 상품 존재 여부 확인
	 *
	 * productId에 해당하는 상품이 존재하면 true, 존재하지 않으면 false를 반환한다.
	 *
	 * @param productId 상품 ID
	 * @return 상품 존재여부
	 */
	@GetMapping("/{productId}/exists")
	ApiResponse<Boolean> exists(@PathVariable UUID productId);

	/**
	 * 상품명으로 상품 조회
	 *
	 * hubId와 productName에 해당하는 상품들을 조회하여 Map 형태로 반환한다.
	 * key는 상품 ID(UUID), value는 ProductDTO 객체이다.
	 *
	 * @param hubId 허브 ID
	 * @param productName 상품명
	 * @return 상품 정보 Map (key: 상품 ID, value: ProductDTO)
	 */
	@GetMapping("/search")
	ApiResponse<Map<UUID, ProductDTO>> searchProducts(@RequestParam UUID hubId, @RequestParam String productName);
}
