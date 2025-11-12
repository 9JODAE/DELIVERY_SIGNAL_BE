package com.delivery_signal.eureka.client.hub.presentation.dto.response;

import com.delivery_signal.eureka.client.hub.application.dto.HubRouteResult;

public record PathResponse(
	String departureHubName,
	String arrivalHubName,
	int transitTime
) {
	public static PathResponse from(HubRouteResult result) {
		return new PathResponse(
			result.departureHubName(),
			result.arrivalHubName(),
			result.transitTime()
		);
	}
}
