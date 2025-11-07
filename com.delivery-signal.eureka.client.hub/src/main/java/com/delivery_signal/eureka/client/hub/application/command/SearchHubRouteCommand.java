package com.delivery_signal.eureka.client.hub.application.command;

/**
 * 허브 이동정보 검색 Command
 */
public record SearchHubRouteCommand(
	String departureHubName,
	String arrivalHubName,
	Integer page,
	Integer size,
	String sortBy,
	String direction
) {
	public static SearchHubRouteCommand of(
		String departureHubName,
		String arrivalHubName,
		Integer page,
		Integer size,
		String sortBy,
		String direction
	) {
		return new SearchHubRouteCommand(departureHubName, arrivalHubName, page, size, sortBy, direction);
	}
}
