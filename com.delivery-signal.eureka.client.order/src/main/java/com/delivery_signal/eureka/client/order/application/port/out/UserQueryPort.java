package com.delivery_signal.eureka.client.order.application.port.out;

import com.delivery_signal.eureka.client.order.domain.vo.user.UserAuthorizationInfo;

/**
 * 사용자 정보 조회 Port (권한)
 */
public interface UserQueryPort {
    UserAuthorizationInfo getUserAuthorizationInfo(Long userId);
}
