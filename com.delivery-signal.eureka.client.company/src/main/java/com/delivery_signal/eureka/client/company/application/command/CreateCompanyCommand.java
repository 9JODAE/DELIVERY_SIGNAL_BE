package com.delivery_signal.eureka.client.company.application.command;

import com.delivery_signal.eureka.client.company.domain.entity.CompanyType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class CreateCompanyCommand {
    private Long userId;
    private UUID hubId;
    private String name;
    private CompanyType type;
    private String address;
    private LocalDateTime createdAt;
}
