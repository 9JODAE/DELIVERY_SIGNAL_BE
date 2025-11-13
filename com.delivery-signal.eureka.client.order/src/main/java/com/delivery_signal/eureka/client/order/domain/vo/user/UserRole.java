package com.delivery_signal.eureka.client.order.domain.vo.user;

public enum UserRole {
    MASTER,           // 관리자 권한
    HUB_MANAGER,      // 허브 관리자 권한
    DELIVERY_MANAGER, // 배송 관리자 권한 (담당자)
    SUPPLIER_MANAGER  // 업체 관리자 권한 (담당자)
}