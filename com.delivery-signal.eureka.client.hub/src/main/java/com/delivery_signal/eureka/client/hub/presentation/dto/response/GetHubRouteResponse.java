package com.delivery_signal.eureka.client.hub.presentation.dto.response;

public record GetHubRouteResponse(
	String departureHubName,
	String arrivalHubName,
	int transitTime,
	double distance
) {}
