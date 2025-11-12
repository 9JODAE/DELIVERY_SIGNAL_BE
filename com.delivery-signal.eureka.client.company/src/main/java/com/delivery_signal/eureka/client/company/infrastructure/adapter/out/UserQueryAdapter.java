package com.delivery_signal.eureka.client.company.infrastructure.adapter.out;

import com.delivery_signal.eureka.client.company.application.port.out.UserQueryPort;
import com.delivery_signal.eureka.client.company.domain.vo.user.UserAuthorizationInfo;
import com.delivery_signal.eureka.client.company.infrastructure.client.UserClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 외부 User 서비스로부터 사용자 인증/활성 정보를 조회하는 어댑터
 */
@Component
@RequiredArgsConstructor
public class UserQueryAdapter implements UserQueryPort {

    private final UserClient userClient;

    // VO 반환
    @Override
    public UserAuthorizationInfo getUserAuthorizationInfo(Long userId) {
        return userClient.getUserAuthorizationInfo(userId);
    }

    // 단순 활성 여부 반환
    @Override
    public boolean isUserApproved(Long userId) {
        UserAuthorizationInfo info = getUserAuthorizationInfo(userId);
        return info.isActive();
    }
}
