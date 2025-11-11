package com.delivery_signal.eureka.client.company.application.result;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class CompanyListResult {
    private UUID id;
    private UUID hubId;
    private String name;
    private String type;
}
