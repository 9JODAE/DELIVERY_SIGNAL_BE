package com.delivery_signal.eureka.client.hub.application.command;

/**
 * 허브 생성 Command
 */
public record CreateHubCommand(
	String name,
	String address,
	Double latitude,
	Double longitude
) {
	public static CreateHubCommand of(
		String name,
		String address,
		Double latitude,
		Double longitude
	) {
		return new CreateHubCommand(name, address, latitude, longitude);
	}
}
