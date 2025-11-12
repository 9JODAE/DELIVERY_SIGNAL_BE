package com.delivery_signal.eureka.client.company.application.command;

import com.delivery_signal.eureka.client.company.domain.entity.CompanyType;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class UpdateCompanyCommand {
    private Long userId;
    private UUID hubId;
    private UUID companyId;
    private String companyName;
    private CompanyType companyType;
    private String address;
}
