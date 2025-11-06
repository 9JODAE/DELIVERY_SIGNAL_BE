package com.delivery_signal.eureka.client.hub.application.command;

import com.delivery_signal.eureka.client.hub.presentation.dto.request.CreateHubRequest;

/**
 * 허브 생성 Command
 */
public record CreateHubCommand(
	String name,
	String address,
	Double latitude,
	Double longitude
) {
	public static CreateHubCommand from(CreateHubRequest request) {
		return new CreateHubCommand(
			request.name(),
			request.address(),
			request.latitude(),
			request.longitude()
		);
	}
}
