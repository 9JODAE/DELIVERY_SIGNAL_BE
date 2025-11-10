package com.delivery_signal.eureka.client.order.application.port.out;

import com.delivery_signal.eureka.client.order.domain.vo.company.CompanyInfo;

import java.util.UUID;

public interface CompanyQueryPort {
    CompanyInfo getCompanyById(UUID companyId);
}

