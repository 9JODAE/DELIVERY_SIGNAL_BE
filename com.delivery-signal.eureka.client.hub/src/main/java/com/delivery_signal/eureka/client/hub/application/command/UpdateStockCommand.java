package com.delivery_signal.eureka.client.hub.application.command;

import java.util.UUID;

/**
 * 재고 수량 수정 Command
 */
public record UpdateStockCommand(
	UUID hubId,
	UUID stockId,
	int quantity
) {
	public static UpdateStockCommand of(
		UUID hubId,
		UUID stockId,
		int quantity
	) {
		return new UpdateStockCommand(hubId, stockId, quantity);
	}
}
