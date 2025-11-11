package com.delivery_signal.eureka.client.hub.presentation.dto.response;

import java.util.UUID;

import com.delivery_signal.eureka.client.hub.application.dto.HubRouteDetailResult;

/**
 * 허브 이동정보 상세 응답 DTO
 */
public record HubRouteDetailResponse(
	UUID hubRouteId,
	UUID departureHubId,
	String departureHubName,
	UUID arrivalHubId,
	String arrivalHubName,
	double distance,
	int transitTime
) {
	public static HubRouteDetailResponse from(HubRouteDetailResult result) {
		return new HubRouteDetailResponse(
			result.hubRouteId(),
			result.departureHubId(),
			result.departureHubName(),
			result.arrivalHubId(),
			result.arrivalHubName(),
			result.distance(),
			result.transitTime()
		);
	}
}
