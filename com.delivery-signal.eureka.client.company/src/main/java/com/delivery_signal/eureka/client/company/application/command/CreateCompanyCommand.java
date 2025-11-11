package com.delivery_signal.eureka.client.company.application.command;

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
    private String type;
    private String address;
    private LocalDateTime createdAt;
}
