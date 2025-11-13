package com.delivery_signal.eureka.client.delivery.domain.vo;

import java.util.UUID;
import lombok.Builder;

/**
 * 사용자 권한 정보 : 인가된 사용자의 필수 정보를 담는 VO
 * - 단일 권한만 가짐
 */
@Builder
public record AuthorizedUser(
    Long userId,
    String role,
    String organization,
    UUID organizationId
) {
    // 비즈니스 로직에서 사용하는 인가 정책을 정의
    public boolean isMaster() {
        return "MASTER".equals(role);
    }

    public boolean isHubManager() {
        return "HUB_MANAGER".equals(role);
    }

    public boolean isDeliveryManager() {
        return "DELIVERY_MANAGER".equals(role);
    }

    public boolean isSupplierManager() {
        return "SUPPLIER_MANAGER".equals(role);
    }
}
