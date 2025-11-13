package com.delivery_signal.eureka.client.company.application.result;

import com.delivery_signal.eureka.client.company.domain.entity.CompanyType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class CompanyDetailResult {
    private UUID companyId;
    private UUID hubId;
    private String companyName;
    private CompanyType type;
    private String address;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
