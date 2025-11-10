package com.delivery_signal.eureka.client.hub.presentation.dto.response;

import java.util.UUID;

/**
 * 재고 생성 응답 DTO
 */
public record CreateStockResponse(
	UUID stockId
) {
	public static CreateStockResponse of(UUID stockId) {
		return new CreateStockResponse(stockId);
	}
}
