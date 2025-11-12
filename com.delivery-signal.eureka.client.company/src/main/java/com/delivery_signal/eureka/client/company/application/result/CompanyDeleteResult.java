package com.delivery_signal.eureka.client.company.application.result;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Builder
public class CompanyDeleteResult {
    private UUID companyId;
    private LocalDateTime deletedAt;
    private Long deletedBy;
    private String message;
}
