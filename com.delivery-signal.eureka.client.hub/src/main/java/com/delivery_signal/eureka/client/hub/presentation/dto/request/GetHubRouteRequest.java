package com.delivery_signal.eureka.client.hub.presentation.dto.request;

import java.util.UUID;

public record GetHubRouteRequest(
	UUID departureHubId,
	UUID arrivalHubId
) {}
