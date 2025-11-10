package com.delivery_signal.eureka.client.order.infrastructure.adapter.out;

import com.delivery_signal.eureka.client.order.application.port.out.UserQueryPort;
import com.delivery_signal.eureka.client.order.domain.vo.user.UserAuthorizationInfo;
import com.delivery_signal.eureka.client.order.infrastructure.client.user.UserClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserQueryAdapter implements UserQueryPort {

    private final UserClient userClient;

    @Override
    public boolean isUserApproved(Long userId) {
        UserAuthorizationInfo info = userClient.getUserAuthorizationInfo(userId);
        return info.isActive(); // 활성 유저만 허용
    }
}
