package com.delivery_signal.eureka.client.user.application.command;

import com.delivery_signal.eureka.client.user.application.dto.UserRoleType;

public record CheckUserRoleCommand (
        Long userId,
        UserRoleType role
){
    public static CheckUserRoleCommand of(Long userId, UserRoleType role){
        return new CheckUserRoleCommand(userId, role);
    }
}
