package com.delivery_signal.eureka.client.company.presentation.external.dto.response;

import com.delivery_signal.eureka.client.company.domain.entity.CompanyType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyUpdateResponseDto {
    private UUID companyId;
    private String companyName;
    private CompanyType type;
    private String address;
    private LocalDateTime updatedAt;
}
