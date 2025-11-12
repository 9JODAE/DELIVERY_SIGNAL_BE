package com.delivery_signal.eureka.client.company.presentation.external.mapper.command;

import com.delivery_signal.eureka.client.company.application.command.CreateCompanyCommand;
import com.delivery_signal.eureka.client.company.common.CompanyTypeConverter;
import com.delivery_signal.eureka.client.company.presentation.external.dto.request.CompanyCreateRequestDto;

public class CompanyCreateMapper {

    public static CreateCompanyCommand toCommand(CompanyCreateRequestDto dto) {
        return CreateCompanyCommand.builder()
                .name(dto.getName())
                .address(dto.getAddress())
                .hubId(dto.getHubId())
                .type(CompanyTypeConverter.toDomain(dto.getType())) // DTO enum → Command enum 변환
                .build();
    }
}
