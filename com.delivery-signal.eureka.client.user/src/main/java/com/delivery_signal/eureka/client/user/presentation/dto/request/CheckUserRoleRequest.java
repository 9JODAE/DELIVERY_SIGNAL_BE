package com.delivery_signal.eureka.client.user.presentation.dto.request;

import com.delivery_signal.eureka.client.user.application.dto.UserRoleType;

public record CheckUserRoleRequest(
        Long userId,
        UserRoleType role
){ }
