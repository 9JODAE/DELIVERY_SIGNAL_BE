package com.delivery_signal.eureka.client.user.application.dto.response;

import com.delivery_signal.eureka.client.user.application.dto.UserRoleType;

public record GetUserRoleResponse (
        Long userId,
        UserRoleType role,
        String extraInfo
){ }
