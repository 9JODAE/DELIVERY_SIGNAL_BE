package com.delivery_signal.eureka.client.user.application.command;

import com.delivery_signal.eureka.client.user.application.dto.UserRoleType;
import com.delivery_signal.eureka.client.user.domain.entity.UserRole;

import java.util.UUID;

public record CreateUserCommand (
        String username,
        String password,
        String slackId,
        String organization,
        UUID organizationId,
        UserRoleType role,
        boolean isMaster,
        String masterToken
){
    public static CreateUserCommand of(
        String username,
        String password,
        String slackId,
        String organization,
        UUID organizationId,
        UserRoleType role,
        boolean isMaster,
        String masterToken
) {
        if (!isMaster || masterToken == null) {
            return new CreateUserCommand(username, password, slackId, organization, organizationId, role, false, null);
        }
        return new CreateUserCommand(username, password, slackId, organization, organizationId, role, isMaster, masterToken);
    }
}
