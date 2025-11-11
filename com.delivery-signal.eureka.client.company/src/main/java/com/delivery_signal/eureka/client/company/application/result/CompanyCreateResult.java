package com.delivery_signal.eureka.client.company.application.result;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Builder
public class CompanyCreateResult {
    private UUID id;
    private UUID hubId;
    private String name;
    private String type;
    private String address;
    private LocalDateTime createdAt;
    private Long createdBy;
    private String message;
}
