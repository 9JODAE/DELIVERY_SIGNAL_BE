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
public class CompanyDeleteResponseDto {
    private UUID companyId;
    private LocalDateTime deletedAt;
    private Long deletedBy;
}
