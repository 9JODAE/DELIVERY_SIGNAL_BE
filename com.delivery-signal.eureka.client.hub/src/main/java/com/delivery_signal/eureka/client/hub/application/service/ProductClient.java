package com.delivery_signal.eureka.client.hub.application.service;

import java.util.UUID;

public interface ProductClient {

	/**
	 * 상품 존재 여부 확인
	 * @param productId 상품 ID
	 * @return 상품 존재 여부
	 */
	boolean existsProduct(UUID productId);
}
