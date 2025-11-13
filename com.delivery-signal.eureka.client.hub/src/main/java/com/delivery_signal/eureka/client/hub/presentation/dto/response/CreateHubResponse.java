package com.delivery_signal.eureka.client.hub.presentation.dto.response;

import java.util.UUID;

/**
 * 허브 생성 응답 DTO
 */
public record CreateHubResponse(
	UUID hubId
) {
	public static CreateHubResponse of(UUID hubId) {
		return new CreateHubResponse(hubId);
	}
}
