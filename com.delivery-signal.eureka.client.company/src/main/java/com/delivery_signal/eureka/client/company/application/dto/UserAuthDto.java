package com.delivery_signal.eureka.client.company.application.dto;

import java.util.UUID;

public record UserAuthDto(
        Long userId,
        String role,
        String organization,
        UUID organizationId
) {
}