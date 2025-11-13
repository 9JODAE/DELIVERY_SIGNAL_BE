package com.delivery_signal.eureka.client.company.infrastructure.adapter.out;

import com.delivery_signal.eureka.client.company.application.dto.ApiResponse;
import com.delivery_signal.eureka.client.company.application.dto.UserAuthDto;
import com.delivery_signal.eureka.client.company.application.port.out.UserQueryPort;
import com.delivery_signal.eureka.client.company.infrastructure.client.UserClient;
import com.delivery_signal.eureka.client.company.infrastructure.client.response.UserAuthResponse;
import com.delivery_signal.eureka.client.company.infrastructure.converter.UserAuthConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 외부 User 서비스로부터 사용자 인증/활성 정보를 조회하는 어댑터
 */
@Component
@RequiredArgsConstructor
public class UserQueryAdapter implements UserQueryPort {

    private final UserClient userClient;
    private final UserAuthConverter userAuthConverter;

    // VO 반환
    @Override
    public UserAuthDto getUserAuthorizationInfo(String userId) {
        ApiResponse<UserAuthResponse> response = userClient.getUserAuthorizationInfo(userId);
        return userAuthConverter.toUserAuthDto(response.data());
    }

}
