package com.delivery_signal.eureka.client.hub.application.dto;

import java.math.BigDecimal;
import java.util.UUID;

import com.delivery_signal.eureka.client.hub.application.dto.external.ProductInfo;
import com.delivery_signal.eureka.client.hub.domain.model.Stock;

/**
 * 재고 상세 결과 DTO (상품 정보 포함)
 */
public record StockDetailResult(
	UUID stockId,
	UUID productId,
	String productName,
	BigDecimal price,
	int quantity
) {
	public static StockDetailResult from(Stock stock, ProductInfo productInfo) {
		return new StockDetailResult(
			stock.getStockId(),
			stock.getProductId().getValue(),
			productInfo.productName(),
			productInfo.price(),
			stock.getQuantity()
		);
	}

	public static StockDetailResult of(
		UUID stockId,
		UUID productId,
		String productName,
		BigDecimal price,
		int quantity
	) {
		return new StockDetailResult(stockId, productId, productName, price, quantity);
	}
}
