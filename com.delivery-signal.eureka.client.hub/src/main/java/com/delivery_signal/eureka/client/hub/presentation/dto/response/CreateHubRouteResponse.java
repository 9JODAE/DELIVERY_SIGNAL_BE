package com.delivery_signal.eureka.client.hub.presentation.dto.response;

import java.util.UUID;

/**
 * 허브 경로 생성 응답 DTO
 */
public record CreateHubRouteResponse(
	UUID hubRouteId
) {
	public static CreateHubRouteResponse of(UUID hubRouteId) {
		return new CreateHubRouteResponse(hubRouteId);
	}
}
