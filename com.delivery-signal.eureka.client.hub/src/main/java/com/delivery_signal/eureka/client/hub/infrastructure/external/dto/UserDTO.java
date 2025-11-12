package com.delivery_signal.eureka.client.hub.infrastructure.external.dto;

import java.util.UUID;

import com.delivery_signal.eureka.client.hub.common.auth.Authority;

public record UserDTO(
	Long userId,
	Authority role,
	String organization,
	UUID organizationId
) {}
