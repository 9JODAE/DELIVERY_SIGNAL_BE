package com.delivery_signal.eureka.client.hub.application.command;

import java.util.UUID;

/**
 * 재고 생성 Command
 */
public record CreateStockCommand(
	UUID hubId,
	UUID productId,
	int quantity
) {
	public static CreateStockCommand of(
		UUID hubId,
		UUID productId,
		int quantity
	) {
		return new CreateStockCommand(hubId, productId, quantity);
	}
}
