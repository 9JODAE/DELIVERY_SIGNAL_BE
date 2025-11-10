package com.delivery_signal.eureka.client.order.application.port.out;

import com.delivery_signal.eureka.client.order.domain.vo.user.UserAuthorizationInfo;

/**
 * 사용자 정보 조회 Port (권한 + 활성 상태)
 */
public interface UserQueryPort {
    UserAuthorizationInfo getUserAuthorizationInfo(Long userId);

    default boolean isUserApproved(Long userId) {
        UserAuthorizationInfo info = getUserAuthorizationInfo(userId);
        return info != null && info.isActive();
    }
}
