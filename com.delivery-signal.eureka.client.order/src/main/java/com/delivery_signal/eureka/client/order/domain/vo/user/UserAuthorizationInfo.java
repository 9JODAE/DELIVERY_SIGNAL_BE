package com.delivery_signal.eureka.client.order.domain.vo.user;

/**
 * 사용자 권한 정보 VO
 * - 단일 권한만 가짐
 * - 활성 여부(active) 확인 가능
 */
public record UserAuthorizationInfo(
        Long userId,
        boolean active,
        String role
) {

    /**
     * 활성 사용자 여부
     */
    public boolean isActive() {
        return active;
    }

    /**
     * 특정 권한 보유 여부 확인
     */
    public boolean hasRole(String expectedRole) {
        return role != null && role.equals(expectedRole);
    }
}

