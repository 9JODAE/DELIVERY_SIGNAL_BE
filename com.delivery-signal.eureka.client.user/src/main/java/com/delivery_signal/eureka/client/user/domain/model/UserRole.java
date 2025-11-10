package com.delivery_signal.eureka.client.user.domain.model;

import lombok.Getter;

@Getter
public enum UserRole {
    MASTER(Authority.MASTER),                       // 관리자 권한
    HUB_MANAGER(Authority.HUB_MANAGER),             // 허브 관리자 권한
    DELIVERY_MANAGER(Authority.DELIVERY_MANAGER),   // 배송 관리자 권한
    SUPPLIER_MANAGER(Authority.SUPPLIER_MANAGER);   // 업체 관리자 권한

    private final String authority;

    UserRole(String authority) {
        this.authority = authority;  // ex) role: "ROLE_OOO"
    }

    public static class Authority {
        public static final String MASTER = "ROLE_MASTER";
        public static final String HUB_MANAGER = "ROLE_HUB_MANAGER";
        public static final String DELIVERY_MANAGER = "ROLE_DELIVERY_MANAGER";
        public static final String SUPPLIER_MANAGER = "ROLE_SUPPLIER_MANAGER";
    }



}
