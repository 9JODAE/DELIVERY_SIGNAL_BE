package com.delivery_signal.eureka.client.company.application.port.out;

import com.delivery_signal.eureka.client.company.application.result.CompanyDetailResult;
import com.delivery_signal.eureka.client.company.application.result.CompanyListResult;

import java.util.List;
import java.util.UUID;

public interface CompanyQueryPort {
    CompanyDetailResult getCompanyById(UUID companyId);
    List<CompanyListResult> getAllCompanies();
    List<CompanyListResult> getCompaniesByHub(UUID hubId);
}
