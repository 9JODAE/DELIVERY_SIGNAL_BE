package com.delivery_signal.eureka.client.order.infrastructure.client.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserAuthorizationResponseDto {
    Long userId;
    String role;
    boolean active;
}
