package com.delivery_signal.eureka.client.company.presentation.external.dto.response;

import com.delivery_signal.eureka.client.company.presentation.external.dto.CompanyTypeDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyListResponseDto {
    private UUID companyId;
    private UUID hubId;
    private String companyName;
    private CompanyTypeDto type;
}
