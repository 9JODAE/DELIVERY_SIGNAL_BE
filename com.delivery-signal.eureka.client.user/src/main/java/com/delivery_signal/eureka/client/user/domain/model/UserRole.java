package com.delivery_signal.eureka.client.user.domain.model;

public enum UserRole {
    MASTER("ROLE_MASTER"),                       // 관리자 권한
    HUB_MANAGER("ROLE_HUB_MANAGER"),             // 허브 관리자 권한
    DELIVERY_MANAGER("ROLE_DELIVERY_MANAGER"),   // 배송 관리자 권한
    SUPPLIER_MANAGER("ROLE_SUPPLIER_MANAGER");   // 업체 관리자 권한

    private final String userRole;

    UserRole(String role) {
        userRole = role;  // ex) role: "ROLE_OOO"
    }

    public String getUserRole() {
        return userRole;
    }

}
