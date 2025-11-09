package com.delivery_signal.eureka.client.hub.application.port;

import java.util.Map;
import java.util.UUID;

import com.delivery_signal.eureka.client.hub.application.dto.external.ProductInfo;

public interface ProductClient {

	/**
	 * 상품 존재 여부 확인
	 * @param productId 상품 ID
	 * @return 상품 존재 여부
	 */
	boolean exists(UUID productId);

	/**
	 * 상품명으로 상품 조회
	 * @param hubId 허브 ID
	 * @param productName 상품명
	 * @return 상품 정보 Map (key: 상품 ID, value: 상품 정보)
	 */
	Map<UUID, ProductInfo> searchProducts(UUID hubId, String productName);
}
