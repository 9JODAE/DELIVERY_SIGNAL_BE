package com.delivery_signal.eureka.client.hub.presentation.dto.response;

import java.util.UUID;

import com.delivery_signal.eureka.client.hub.application.dto.StockResult;

public record StockResponse(
	UUID stockId,
	UUID productId,
	String productName
) {
	public static StockResponse from(StockResult result) {
		return new StockResponse(
			result.stockId(),
			result.productId(),
			result.productName()
		);
	}
}
