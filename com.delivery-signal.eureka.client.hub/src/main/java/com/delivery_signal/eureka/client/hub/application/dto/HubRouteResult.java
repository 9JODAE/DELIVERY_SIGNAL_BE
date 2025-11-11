package com.delivery_signal.eureka.client.hub.application.dto;

import com.delivery_signal.eureka.client.hub.domain.model.HubRoute;

public record HubRouteResult(
	String departureHubName,
	String arrivalHubName,
	int transitTime
) {
	public static HubRouteResult from(HubRoute hubRoute) {
		return new HubRouteResult(
			hubRoute.getDepartureHub().getName(),
			hubRoute.getArrivalHub().getName(),
			hubRoute.getTransitTime().getMinutes()
		);
	}
}
