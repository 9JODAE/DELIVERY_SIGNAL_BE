package com.delivery_signal.eureka.client.company.presentation.external.dto.request;

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
    private String type;
    private String address;
}

