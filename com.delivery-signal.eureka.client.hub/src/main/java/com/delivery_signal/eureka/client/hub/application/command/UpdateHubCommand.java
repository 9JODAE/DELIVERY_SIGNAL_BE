package com.delivery_signal.eureka.client.hub.application.command;

import java.util.UUID;

import com.delivery_signal.eureka.client.hub.presentation.dto.request.UpdateHubRequest;

public record UpdateHubCommand(
	UUID hubId,
	String name,
	String address,
	Double latitude,
	Double longitude
) {
	public static UpdateHubCommand of(UUID hubId, UpdateHubRequest request) {
		return new UpdateHubCommand(
			hubId,
			request.name(),
			request.address(),
			request.latitude(),
			request.longitude()
		);
	}
}