package com.delivery_signal.eureka.client.company.application.command;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class UpdateCompanyCommand {
    private Long userId;
    private UUID companyId;
    private String name;
    private String type;
    private String address;
}
