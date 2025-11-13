package com.delivery_signal.eureka.client.external.slack.application.service;

import com.delivery_signal.eureka.client.external.slack.application.port.UserConnector;
import com.delivery_signal.eureka.client.external.slack.common.exception.ErrorCode;
import com.delivery_signal.eureka.client.external.slack.common.exception.GlobalException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserValidationService {

    private final UserConnector userConnector;

    /**
     * SlackRecord 권한 확인
     * @param userId 유저식별자
     */
    public void validateSlackRecordAuthorization(Long userId) {
        String userRole = userConnector.getUserAuthorizationInfo(userId).getRole();
        if (!(userRole.equals("MASTER") || userRole.equals("HUB_MANAGER"))) {
            throw new GlobalException(ErrorCode.FORBIDDEN_SLACK_RECORD);
        }
    }

}
