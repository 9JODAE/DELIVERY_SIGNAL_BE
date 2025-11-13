package com.delivery_signal.eureka.client.hub.application.port;

import com.delivery_signal.eureka.client.hub.application.dto.external.UserInfo;

public interface UserClient {

	UserInfo getUserInfo(String userId);
}
