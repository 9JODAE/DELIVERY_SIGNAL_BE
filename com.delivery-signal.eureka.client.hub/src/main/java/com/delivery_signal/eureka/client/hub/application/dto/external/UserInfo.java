package com.delivery_signal.eureka.client.hub.application.dto.external;

import java.util.UUID;

public record UserInfo(
	Long userId,
	String role,
	String organization,
	UUID organizationId
) {
}
