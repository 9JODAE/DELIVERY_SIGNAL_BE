package com.delivery_signal.eureka.client.company.presentation.external.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyUpdateResponseDto {
    private UUID id;
    private String name;
    private String type;
    private String address;
    private LocalDateTime updatedAt;
}
