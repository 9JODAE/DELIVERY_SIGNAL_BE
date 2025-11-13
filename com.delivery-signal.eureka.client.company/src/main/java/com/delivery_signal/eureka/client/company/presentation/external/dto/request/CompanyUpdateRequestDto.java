package com.delivery_signal.eureka.client.company.presentation.external.dto.request;

import com.delivery_signal.eureka.client.company.presentation.external.dto.CompanyTypeDto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyUpdateRequestDto {
    private String companyName;
    private CompanyTypeDto type;
    private String address;
}
