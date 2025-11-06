package com.delivery_signal.eureka.client.hub.application.command;

import java.util.UUID;

/**
 * 허브 수정 Command
 */
public record UpdateHubCommand(
	UUID hubId,
	String name,
	String address,
	Double latitude,
	Double longitude
) {
	public static UpdateHubCommand of(
		UUID hubId,
		String name,
		String address,
		Double latitude,
		Double longitude
	) {
		return new UpdateHubCommand(hubId, name, address, latitude, longitude);
	}
}