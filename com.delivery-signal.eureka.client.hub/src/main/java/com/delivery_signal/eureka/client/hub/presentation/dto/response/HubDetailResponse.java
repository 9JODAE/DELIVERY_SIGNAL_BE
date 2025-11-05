package com.delivery_signal.eureka.client.hub.presentation.dto.response;

import java.util.UUID;

import com.delivery_signal.eureka.client.hub.application.dto.HubResult;

public record HubDetailResponse(
	UUID hubId,
	String name,
	String address,
	double latitude,
	double longitude
) {
	public static HubDetailResponse from(HubResult result) {
		return new HubDetailResponse(
			result.hubId(),
			result.name(),
			result.address(),
			result.latitude(),
			result.longitude()
		);
	}
}
