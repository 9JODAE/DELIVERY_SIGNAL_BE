package com.delivery_signal.eureka.client.user.application.dto.response;

import com.delivery_signal.eureka.client.user.application.dto.UserRoleType;

import java.util.UUID;

public record GetUserAuthorizationResponse(
   Long userId,
   UserRoleType role,
   String organization,
   UUID organizationId
){ }
