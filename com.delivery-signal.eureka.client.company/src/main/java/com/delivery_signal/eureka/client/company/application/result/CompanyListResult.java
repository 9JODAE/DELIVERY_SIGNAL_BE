package com.delivery_signal.eureka.client.company.application.result;

import com.delivery_signal.eureka.client.company.domain.entity.CompanyType;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class CompanyListResult {
    private UUID companyId;
    private UUID hubId;
    private String companyName;
    private CompanyType type;
}
