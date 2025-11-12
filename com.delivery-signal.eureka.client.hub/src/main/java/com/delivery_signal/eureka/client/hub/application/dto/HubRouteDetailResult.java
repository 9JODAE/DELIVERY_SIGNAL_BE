package com.delivery_signal.eureka.client.hub.application.dto;

import java.util.UUID;

import com.delivery_signal.eureka.client.hub.domain.model.HubRoute;

/**
 * 허브 이동정보 조회 결과 DTO
 */
public record HubRouteDetailResult(
	UUID hubRouteId,
	UUID departureHubId,
	String departureHubName,
	UUID arrivalHubId,
	String arrivalHubName,
	double distance,
	int transitTime
) {
	public  static HubRouteDetailResult from(HubRoute hubRoute) {
		return new HubRouteDetailResult(
			hubRoute.getHubRouteId(),
			hubRoute.getDepartureHub().getHubId(),
			hubRoute.getDepartureHub().getName(),
			hubRoute.getArrivalHub().getHubId(),
			hubRoute.getArrivalHub().getName(),
			hubRoute.getDistance().getKilometers(),
			hubRoute.getTransitTime().getMinutes()
		);
	}
}
