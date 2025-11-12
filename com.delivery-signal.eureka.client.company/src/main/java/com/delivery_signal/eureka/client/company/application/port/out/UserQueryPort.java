package com.delivery_signal.eureka.client.company.application.port.out;

import com.delivery_signal.eureka.client.company.domain.vo.user.UserAuthorizationInfo;

public interface UserQueryPort {
    boolean isUserApproved(Long userId);

    UserAuthorizationInfo getUserAuthorizationInfo(Long userId);
}
