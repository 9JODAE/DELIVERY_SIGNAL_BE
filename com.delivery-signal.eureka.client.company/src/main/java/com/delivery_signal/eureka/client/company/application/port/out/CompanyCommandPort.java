package com.delivery_signal.eureka.client.company.application.port.out;

import com.delivery_signal.eureka.client.company.application.command.CreateCompanyCommand;
import com.delivery_signal.eureka.client.company.application.command.DeleteCompanyCommand;
import com.delivery_signal.eureka.client.company.application.command.UpdateCompanyCommand;
import com.delivery_signal.eureka.client.company.application.result.CompanyCreateResult;
import com.delivery_signal.eureka.client.company.application.result.CompanyDeleteResult;
import com.delivery_signal.eureka.client.company.application.result.CompanyUpdateResult;

public interface CompanyCommandPort {

    CompanyCreateResult createCompany(CreateCompanyCommand command);

    CompanyUpdateResult updateCompany(UpdateCompanyCommand command);

    CompanyDeleteResult deleteCompany(DeleteCompanyCommand command);
}
