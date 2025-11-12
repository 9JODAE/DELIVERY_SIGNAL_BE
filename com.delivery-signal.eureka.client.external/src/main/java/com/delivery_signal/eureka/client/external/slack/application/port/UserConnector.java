package com.delivery_signal.eureka.client.external.slack.application.port;

import com.delivery_signal.eureka.client.external.slack.application.dto.UserAuthorizationInfoDto;

public interface UserConnector {
    UserAuthorizationInfoDto getUserAuthorizationInfo(Long userId);
}