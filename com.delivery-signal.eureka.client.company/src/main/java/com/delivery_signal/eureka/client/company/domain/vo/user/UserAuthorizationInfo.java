package com.delivery_signal.eureka.client.company.domain.vo.user;

import com.delivery_signal.eureka.client.company.application.port.out.HubQueryPort;
import com.delivery_signal.eureka.client.company.application.port.out.UserQueryPort;
import jakarta.ws.rs.ForbiddenException;
import lombok.Builder;
import lombok.Getter;
import org.springframework.stereotype.Component;

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