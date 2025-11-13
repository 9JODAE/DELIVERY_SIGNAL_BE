package com.delivery_signal.eureka.client.company.domain.vo.user;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class UserAuthorizationInfo {
    private Long userId;
    private String role;
    private UUID hubId;
    private UUID companyId;
    private boolean active;
}