package com.delivery_signal.eureka.client.user.application.dto.response;

import com.delivery_signal.eureka.client.user.application.dto.ApprovalStatusType;
import com.delivery_signal.eureka.client.user.application.dto.UserRoleType;

import java.util.UUID;

public record GetUserResponse(
        Long userId,
        String username,
        String slackId,
        String organization,
        UUID organizationId,
        UserRoleType role,
        ApprovalStatusType approvalStatus
){}
