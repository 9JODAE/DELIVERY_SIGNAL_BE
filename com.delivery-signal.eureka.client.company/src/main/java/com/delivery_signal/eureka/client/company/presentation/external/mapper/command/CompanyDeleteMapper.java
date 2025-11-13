package com.delivery_signal.eureka.client.company.presentation.external.mapper.command;

import com.delivery_signal.eureka.client.company.application.command.DeleteCompanyCommand;

import java.util.UUID;

public class CompanyDeleteMapper {

    public static DeleteCompanyCommand toCommand(UUID companyId) {
        return DeleteCompanyCommand.builder()
                .companyId(companyId)
                .build();
    }
}
