package com.delivery_signal.eureka.client.hub.application.command;

import java.util.UUID;

public record UpdateHubRouteCommand(
	UUID departureHubId,
	UUID hubRouteId,
	double distance,
	int transitTime
) {
	public static UpdateHubRouteCommand of(UUID departureHubId, UUID hubRouteId, double distance, int transitTime) {
		return new UpdateHubRouteCommand(departureHubId, hubRouteId, distance, transitTime);
	}
}
