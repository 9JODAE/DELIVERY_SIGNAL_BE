package com.delivery_signal.eureka.client.external.slack.application.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserAuthorizationInfoDto {
    private Long userId;
    private String role;
}
