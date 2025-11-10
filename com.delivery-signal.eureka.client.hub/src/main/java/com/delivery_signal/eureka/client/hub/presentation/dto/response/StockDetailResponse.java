package com.delivery_signal.eureka.client.hub.presentation.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

import com.delivery_signal.eureka.client.hub.application.dto.StockDetailResult;

public record StockDetailResponse(
	UUID stockId,
	UUID productId,
	String productName,
	BigDecimal price,
	int quantity
) {
	public static StockDetailResponse from(StockDetailResult result) {
		return new StockDetailResponse(
			result.stockId(),
			result.productId(),
			result.productName(),
			result.price(),
			result.quantity()
		);
	}
}
