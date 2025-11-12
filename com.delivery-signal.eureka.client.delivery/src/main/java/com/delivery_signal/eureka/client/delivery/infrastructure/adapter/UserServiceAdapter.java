package com.delivery_signal.eureka.client.delivery.infrastructure.adapter;

import com.delivery_signal.eureka.client.delivery.application.port.UserAuthPort;
import com.delivery_signal.eureka.client.delivery.application.service.UserServiceClient;
import com.delivery_signal.eureka.client.delivery.domain.vo.AuthorizedUser;
import org.springframework.stereotype.Component;

/**
 * ACL: 외부 User 서비스로부터 사용자 인증/활성 정보를 조회하는 어댑터
 */
@Component
public class UserServiceAdapter implements UserAuthPort {
    private final UserServiceClient userServiceClient;

    public UserServiceAdapter(UserServiceClient userServiceClient) {
        this.userServiceClient = userServiceClient;
    }

    /**
     * User Service로부터 인가에 필요한 상세 정보를 조회하여 Domain VO로 변환
     */
    @Override
    public AuthorizedUser fetchUserAuthorizationInfo(Long userId) {
        // FeignClient를 통해 User Service 호출 (인프라 상세 구현)
        UserServiceClient.AuthUserResponseDto response =
            userServiceClient.getUserAuthorizationInfo(userId);

        // DTO -> Domain VO 변환 (ACL 역할 수행)
        return new AuthorizedUser(
            response.userId(),
            response.role(),
            response.organization(),
            response.organizationId()
        );
    }
}
