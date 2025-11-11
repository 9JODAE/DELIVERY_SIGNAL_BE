package com.delivery_signal.eureka.client.company.presentation.external.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyUpdateRequestDto {
    private String name;
    private String type;
    private String address;
}
