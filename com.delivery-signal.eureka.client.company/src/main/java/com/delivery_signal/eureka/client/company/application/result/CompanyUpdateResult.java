package com.delivery_signal.eureka.client.company.application.result;

import com.delivery_signal.eureka.client.company.domain.entity.CompanyType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class CompanyUpdateResult {
    private UUID companyId;
    private String companyName;
    private CompanyType type;
    private String address;
    private Long updatedBy;
    private LocalDateTime updatedAt;
}
