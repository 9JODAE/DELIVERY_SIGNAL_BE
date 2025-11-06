package com.delivery_signal.eureka.client.hub.presentation.dto.response;

import java.util.UUID;

/**
 * 허브 생성 응답 DTO
 */
public record HubCreateResponse(
	UUID hubId
) {
	public static HubCreateResponse of(UUID hubId) {
		return new HubCreateResponse(hubId);
	}
}
