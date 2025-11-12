package com.delivery_signal.eureka.client.hub.application.command;

import java.util.UUID;

public record GetHubRouteCommand(
	UUID departureHubId,
	UUID arrivalHubId
) {
	public static GetHubRouteCommand of(UUID departureHubId, UUID arrivalHubId) {
		return new GetHubRouteCommand(departureHubId, arrivalHubId);
	}
}
