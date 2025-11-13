package com.delivery_signal.eureka.client.hub.application.command;

import java.util.Map;
import java.util.UUID;

public record RestoreStockQuantityCommand(
	UUID hubId,
	Map<UUID, Integer> items
) {
	public static RestoreStockQuantityCommand of(
		UUID hubId,
		Map<UUID, Integer> items
	) {
		return new RestoreStockQuantityCommand(hubId, items);
	}
}
