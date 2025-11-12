package com.delivery_signal.eureka.client.user.application.dto;

import com.delivery_signal.eureka.client.user.domain.entity.UserRole;

public enum UserRoleType {
    MASTER, HUB_MANAGER, DELIVERY_MANAGER, SUPPLIER_MANAGER;

    public static UserRoleType from(UserRole role) {
        return UserRoleType.valueOf(role.name());
    }

    public UserRole toDomain() {
        return UserRole.valueOf(this.name());
    }
}
