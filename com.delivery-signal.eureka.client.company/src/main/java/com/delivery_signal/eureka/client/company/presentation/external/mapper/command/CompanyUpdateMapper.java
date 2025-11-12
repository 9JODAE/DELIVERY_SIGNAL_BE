package com.delivery_signal.eureka.client.company.presentation.external.mapper.command;

import com.delivery_signal.eureka.client.company.application.command.UpdateCompanyCommand;
import com.delivery_signal.eureka.client.company.common.CompanyTypeConverter;
import com.delivery_signal.eureka.client.company.presentation.external.dto.request.CompanyUpdateRequestDto;

import java.util.UUID;

public class CompanyUpdateMapper {

    public static UpdateCompanyCommand toCommand(UUID companyId, CompanyUpdateRequestDto dto) {
        return UpdateCompanyCommand.builder()
                .companyId(companyId)
                .companyName(dto.getCompanyName())
                .address(dto.getAddress())
                .companyType(CompanyTypeConverter.toDomain(dto.getType())) // DTO enum → Command/Domain enum 변환
                .build();
    }
}
