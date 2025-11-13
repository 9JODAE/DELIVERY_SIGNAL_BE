package com.delivery_signal.eureka.client.company.presentation.external.dto.request;

import com.delivery_signal.eureka.client.company.presentation.external.dto.CompanyTypeDto;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyCreateRequestDto {
    private UUID hubId;
    private String name;
    private CompanyTypeDto type;
    private String address;
}

