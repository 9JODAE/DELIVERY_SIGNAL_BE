package com.delivery_signal.eureka.client.user.presentation.dto.response;

import com.delivery_signal.eureka.client.user.domain.model.ApprovalStatus;
import com.delivery_signal.eureka.client.user.domain.model.UserRole;

import java.util.UUID;

public record UserResponseDto (
        Long userId,
        String username,
        String slackId,
        String organization,
        UUID organizationId,
        UserRole role,
        ApprovalStatus approvalStatus
){}
