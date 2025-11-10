package com.delivery_signal.eureka.client.hub.application.command;

import java.util.UUID;

/**
 * 재고 검색 Command
 */
public record SearchStockCommand(
	UUID hubId,
	String productName,
	Integer page,
	Integer size,
	String sortBy,
	String direction
) {
	public static SearchStockCommand of(
		UUID hubId,
		String productName,
		Integer page,
		Integer size,
		String sortBy,
		String direction
	) {
		return new SearchStockCommand(hubId, productName, page, size, sortBy, direction);
	}
}
