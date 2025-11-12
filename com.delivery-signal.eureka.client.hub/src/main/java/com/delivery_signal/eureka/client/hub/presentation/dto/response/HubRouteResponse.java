package com.delivery_signal.eureka.client.hub.presentation.dto.response;

import java.util.UUID;

import com.delivery_signal.eureka.client.hub.application.dto.HubRouteDetailResult;

/**
 * 허브 이동정보 응답 DTO
 */
public record HubRouteResponse(
	UUID hubRouteId,
	UUID departureHubId,
	String departureHubName,
	UUID arrivalHubId,
	String arrivalHubName
) {
	public static HubRouteResponse from(HubRouteDetailResult hubRouteResult) {
		return new HubRouteResponse(
			hubRouteResult.hubRouteId(),
			hubRouteResult.departureHubId(),
			hubRouteResult.departureHubName(),
			hubRouteResult.arrivalHubId(),
			hubRouteResult.arrivalHubName()
		);
	}
}
