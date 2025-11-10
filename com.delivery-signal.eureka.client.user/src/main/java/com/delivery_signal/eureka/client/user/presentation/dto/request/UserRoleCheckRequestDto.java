package com.delivery_signal.eureka.client.user.presentation.dto.request;

import com.delivery_signal.eureka.client.user.domain.model.UserRole;

public record UserRoleCheckRequestDto(
        Long userId,
        UserRole role
){ }
