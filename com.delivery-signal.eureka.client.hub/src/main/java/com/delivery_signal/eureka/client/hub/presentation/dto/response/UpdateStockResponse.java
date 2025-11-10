package com.delivery_signal.eureka.client.hub.presentation.dto.response;

import java.util.UUID;

import com.delivery_signal.eureka.client.hub.application.dto.StockResult;

public record UpdateStockResponse(
	UUID stockId,
	UUID productId,
	int quantity
) {
	public static UpdateStockResponse from(StockResult result) {
		return new UpdateStockResponse(
			result.stockId(),
			result.productId(),
			result.quantity()
		);
	}
}
