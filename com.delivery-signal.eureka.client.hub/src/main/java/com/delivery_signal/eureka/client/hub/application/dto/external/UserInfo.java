package com.delivery_signal.eureka.client.hub.application.dto.external;

import java.util.UUID;

import com.delivery_signal.eureka.client.hub.common.auth.Authority;

public record UserInfo(
	Long userId,
	Authority role,
	String organization,
	UUID organizationId
) {
}
