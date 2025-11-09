package com.delivery_signal.eureka.client.hub.application.dto;

import java.math.BigDecimal;
import java.util.UUID;

import com.delivery_signal.eureka.client.hub.application.dto.external.ProductInfo;
import com.delivery_signal.eureka.client.hub.domain.model.Stock;

/**
 * 재고 결과 DTO
 */
public record StockResult(
	UUID stockId,
	UUID productId,
	String productName,
	BigDecimal price,
	int quantity
) {
	public static StockResult from(Stock stock, ProductInfo productInfo) {
		return new StockResult(
			stock.getStockId(),
			stock.getProductId().getValue(),
			productInfo.productName(),
			productInfo.price(),
			stock.getQuantity()
		);
	}

	public static StockResult of(
		UUID stockId,
		UUID productId,
		String productName,
		BigDecimal price,
		int quantity
	) {
		return new StockResult(
			stockId,
			productId,
			productName,
			price,
			quantity
		);
	}
}
