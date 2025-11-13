package com.delivery_signal.eureka.client.hub.domain.condition;

import java.util.List;
import java.util.UUID;

public record StockSearchCondition(
	UUID hubId,
	List<UUID> productIds,
	Integer page,
	Integer size,
	String sortBy,
	String direction
) {
	public static StockSearchCondition of(
		UUID hubId,
		List<UUID> productIds,
		Integer page,
		Integer size,
		String sortBy,
		String direction
	) {
		return new StockSearchCondition(hubId, productIds, page, size, sortBy, direction);
	}
}
