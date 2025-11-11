package com.delivery_signal.eureka.client.order.domain.vo.user;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

/**
 * 사용자 권한 정보 VO
 * - 단일 권한만 가짐
 * - 활성 여부(active) 확인 가능
 */
@Getter
@Builder
public class UserAuthorizationInfo {
    private final Long userId;
    private final boolean active;
    private final String role;
    private final UUID hubId; // 허브 정보 포함 (인가 판단용)

    /**
     * 특정 권한 보유 여부 확인
     */
    public boolean hasRole(String expectedRole) {
        return role != null && role.equals(expectedRole);
    }
}

