package com.delivery_signal.eureka.client.hub.domain.condition;

public record HubRouteSearchCondition(
	String departureHubName,
	String arrivalHubName,
	Integer page,
	Integer size,
	String sortBy,
	String direction
) {
	public static HubRouteSearchCondition of(
		String departureHubName,
		String arrivalHubName,
		Integer page,
		Integer size,
		String sortBy,
		String direction
	){
		return new HubRouteSearchCondition(departureHubName, arrivalHubName, page, size, sortBy, direction);
	}
}
