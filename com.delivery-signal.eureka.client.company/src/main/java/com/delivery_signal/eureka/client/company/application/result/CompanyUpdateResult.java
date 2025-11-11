package com.delivery_signal.eureka.client.company.application.result;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Builder
public class CompanyUpdateResult {
    private UUID id;
    private String name;
    private String type;
    private String address;
    private LocalDateTime updatedAt;
}
