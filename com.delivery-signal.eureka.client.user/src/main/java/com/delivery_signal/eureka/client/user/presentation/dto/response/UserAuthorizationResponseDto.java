package com.delivery_signal.eureka.client.user.presentation.dto.response;

import com.delivery_signal.eureka.client.user.domain.model.UserRole;

public record UserAuthorizationResponseDto (
   Long userId,
   UserRole role,
   String extraInfo
){ }
