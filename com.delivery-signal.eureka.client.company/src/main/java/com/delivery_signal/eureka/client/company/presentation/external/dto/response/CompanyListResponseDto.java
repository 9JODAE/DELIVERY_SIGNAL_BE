package com.delivery_signal.eureka.client.company.presentation.external.dto.response;

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
    private UUID id;
    private UUID hubId;
    private String name;
    private String type;
}
