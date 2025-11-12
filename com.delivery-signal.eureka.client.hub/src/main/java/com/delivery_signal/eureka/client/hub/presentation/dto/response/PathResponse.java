package com.delivery_signal.eureka.client.hub.presentation.dto.response;

import java.util.UUID;

import com.delivery_signal.eureka.client.hub.application.dto.HubRouteResult;

public record PathResponse(
	UUID departureHubId,
	String departureHubName,
	UUID arrivalHubId,
	String arrivalHubName,
	double distance,
	int transitTime
) {
	public static PathResponse from(HubRouteResult result) {
		return new PathResponse(
			result.departureHubId(),
			result.departureHubName(),
			result.arrivalHubId(),
			result.arrivalHubName(),
			result.distance(),
			result.transitTime()
		);
	}
}
