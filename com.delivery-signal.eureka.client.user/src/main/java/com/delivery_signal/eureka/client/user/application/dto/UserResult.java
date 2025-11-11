package com.delivery_signal.eureka.client.user.application.dto;

import com.delivery_signal.eureka.client.user.domain.entity.ApprovalStatus;
import com.delivery_signal.eureka.client.user.domain.entity.User;
import com.delivery_signal.eureka.client.user.domain.entity.UserRole;

import java.util.UUID;

public record UserResult (
        Long userId,
        String username,
        String slackId,
        String organization,
        UUID organizationId,
        UserRole role,
        ApprovalStatusType approvalStatus
) {
    public static UserResult from(User user) {
        return new UserResult(
                user.getUserId(),
                user.getUsername(),
                user.getSlackId(),
                user.getOrganization(),
                user.getOrganizationId(),
                user.getRole(),
                ApprovalStatusType.from(user.getApprovalStatus())
        );
    }
}
