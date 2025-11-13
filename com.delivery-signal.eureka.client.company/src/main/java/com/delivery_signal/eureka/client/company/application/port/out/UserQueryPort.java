package com.delivery_signal.eureka.client.company.application.port.out;

import com.delivery_signal.eureka.client.company.application.dto.UserAuthDto;


public interface UserQueryPort {

    UserAuthDto getUserAuthorizationInfo(String userId);
}