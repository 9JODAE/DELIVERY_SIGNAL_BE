package com.delivery_signal.eureka.client.company.application.service;

import com.delivery_signal.eureka.client.company.application.result.OrderCompanyResult;
import com.delivery_signal.eureka.client.company.domain.entity.Company;
import com.delivery_signal.eureka.client.company.domain.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Order 서비스 통신용 Internal Company Service
 */
@Service
@RequiredArgsConstructor
public class InternalOrderCompanyService {

    private final CompanyRepository companyRepository;

    public OrderCompanyResult getCompanyInfo(UUID companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found: " + companyId));

        return OrderCompanyResult.builder()
                .companyId(company.getCompanyId())
                .hubId(company.getHubId())
                .address(company.getAddress())
                .build();
    }
}
