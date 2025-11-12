package com.delivery_signal.eureka.client.hub.infrastructure.external.dto;

import java.util.UUID;

public record UserDTO(
	Long userId,
	String role,
	String organization,
	UUID organizationId
) {}
