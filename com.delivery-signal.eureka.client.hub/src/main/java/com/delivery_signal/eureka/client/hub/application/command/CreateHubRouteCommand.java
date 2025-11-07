package com.delivery_signal.eureka.client.hub.application.command;

import java.util.UUID;

/**
 * 허브 경로 생성 Command
 */
public record CreateHubRouteCommand(
	UUID departureHubId,
	UUID arrivalHubId,
	double distance,
	int transitTime
) {
	public static CreateHubRouteCommand of(
		UUID departureHubId,
		UUID arrivalHubId,
		double distance,
		int transitTime
	){
		return new CreateHubRouteCommand(departureHubId, arrivalHubId, distance, transitTime);
	}
}
