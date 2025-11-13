package com.delivery_signal.eureka.client.external.slack.infrastructure.user.dto;

import com.delivery_signal.eureka.client.external.slack.application.dto.UserAuthorizationInfoDto;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserAuthorizationResponse {
    private Long userId;
    private String role;

    public  UserAuthorizationInfoDto toDto(){
        return UserAuthorizationInfoDto.builder()
                .userId(userId)
                .role(role)
                .build();
    }
}
