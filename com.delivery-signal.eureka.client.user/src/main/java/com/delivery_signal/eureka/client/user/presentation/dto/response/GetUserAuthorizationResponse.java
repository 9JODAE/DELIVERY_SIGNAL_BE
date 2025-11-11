package com.delivery_signal.eureka.client.user.presentation.dto.response;

import com.delivery_signal.eureka.client.user.application.dto.UserRoleType;

public record GetUserAuthorizationResponse(
   Long userId,
   UserRoleType role,
   String extraInfo
){ }
