package com.delivery_signal.eureka.client.user.presentation.dto.response;

import com.delivery_signal.eureka.client.user.application.dto.UserRoleType;

import java.util.UUID;

public record GetUserRoleResponse (
        Long userId,
        UserRoleType role,
        String extraInfo
){ }
