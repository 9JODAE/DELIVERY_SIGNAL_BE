package com.delivery_signal.eureka.client.user.presentation.dto.response;

import com.delivery_signal.eureka.client.user.domain.model.ApprovalStatus;
import com.delivery_signal.eureka.client.user.domain.model.UserRole;

public record UserResponseDto (
        Long userId,
        String username,
        String slackId,
        String organization,
        UserRole role,
        ApprovalStatus approvalStatus
){}
