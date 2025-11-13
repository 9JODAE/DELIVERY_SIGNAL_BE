package com.delivery_signal.eureka.client.hub.application.dto;

import java.util.UUID;

import com.delivery_signal.eureka.client.hub.domain.entity.Stock;

/**
 * 재고 결과 DTO
 */
public record StockResult(
	UUID stockId,
	UUID productId,
	int quantity
) {
	public static StockResult from(Stock stock) {
		return new StockResult(
			stock.getStockId(),
			stock.getProductId().getValue(),
			stock.getQuantity()
		);
	}
}
