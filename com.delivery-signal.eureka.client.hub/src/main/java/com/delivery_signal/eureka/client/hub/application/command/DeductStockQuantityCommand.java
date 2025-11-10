package com.delivery_signal.eureka.client.hub.application.command;

import java.util.Map;
import java.util.UUID;

public record DeductStockQuantityCommand(
	UUID hubId,
	Map<UUID, Integer> items
) {
	public static DeductStockQuantityCommand of(
		UUID hubId,
		Map<UUID, Integer> items
	) {
		return new DeductStockQuantityCommand(hubId, items);
	}
}
