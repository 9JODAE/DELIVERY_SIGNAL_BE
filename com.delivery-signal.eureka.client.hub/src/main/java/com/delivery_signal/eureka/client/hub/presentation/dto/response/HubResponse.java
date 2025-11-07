package com.delivery_signal.eureka.client.hub.presentation.dto.response;

import java.util.UUID;

import com.delivery_signal.eureka.client.hub.application.dto.HubResult;

/**
 * 허브 응답 DTO
 */
public record HubResponse(
	UUID hubId,
	String name,
	String address
) {
	public static HubResponse from(HubResult result) {
		return new HubResponse(
			result.hubId(),
			result.name(),
			result.address()
		);
	}
}
