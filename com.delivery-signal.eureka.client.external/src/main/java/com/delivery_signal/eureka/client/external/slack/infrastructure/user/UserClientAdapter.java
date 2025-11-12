package com.delivery_signal.eureka.client.external.slack.infrastructure.user;

import com.delivery_signal.eureka.client.external.slack.application.dto.UserAuthorizationInfoDto;
import com.delivery_signal.eureka.client.external.slack.application.port.UserConnector;
import com.delivery_signal.eureka.client.external.slack.infrastructure.user.dto.UserAuthorizationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserClientAdapter implements UserConnector {

    private final UserServiceClient feignClient;

    @Override
    public UserAuthorizationInfoDto getUserAuthorizationInfo(Long userId) {
        UserAuthorizationResponse response = feignClient.getUserAuthorizationInfo(userId);
        return response.toDto();
    }

}