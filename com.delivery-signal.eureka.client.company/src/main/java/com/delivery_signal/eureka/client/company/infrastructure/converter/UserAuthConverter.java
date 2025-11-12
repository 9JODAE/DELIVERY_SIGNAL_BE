package com.delivery_signal.eureka.client.company.infrastructure.converter;

import com.delivery_signal.eureka.client.company.application.dto.UserAuthDto;
import com.delivery_signal.eureka.client.company.infrastructure.client.response.UserAuthResponse;
import org.springframework.stereotype.Component;

/**
 * 외부 데이터를 내부 DTO로 변환하는 책임 객체
 */
@Component
public class UserAuthConverter {

    public UserAuthDto toUserAuthDto(UserAuthResponse response) {

        return new UserAuthDto(
                response.userId(),
                response.role(),
                response.organization(),
                response.organizationId()
        );
    }
}