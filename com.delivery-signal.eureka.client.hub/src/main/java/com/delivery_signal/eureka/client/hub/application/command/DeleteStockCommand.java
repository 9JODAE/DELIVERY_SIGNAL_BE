package com.delivery_signal.eureka.client.hub.application.command;

import java.util.UUID;

public record DeleteStockCommand(
	UUID hubId,
	UUID stockId
) {
	public static DeleteStockCommand of(
		UUID hubId,
		UUID stockId
	) {
		return new DeleteStockCommand(hubId, stockId);
	}
}
