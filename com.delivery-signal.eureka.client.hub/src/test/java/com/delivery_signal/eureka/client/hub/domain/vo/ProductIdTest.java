package com.delivery_signal.eureka.client.hub.domain.vo;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductIdTest {

	@Test
	@DisplayName("정상: 유효한 UUID로 ProductId 생성 (of)")
	void createWithFactoryMethod() {
		UUID id = UUID.randomUUID();
		ProductId productId = ProductId.of(id);
		assertNotNull(productId);
	}

	@Test
	@DisplayName("예외: null 전달 시 of에서 IllegalArgumentException 발생")
	void factoryMethodNullThrows() {
		IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> ProductId.of(null));
		assertEquals("유효하지 않은 상품 ID입니다", ex.getMessage());
	}
}