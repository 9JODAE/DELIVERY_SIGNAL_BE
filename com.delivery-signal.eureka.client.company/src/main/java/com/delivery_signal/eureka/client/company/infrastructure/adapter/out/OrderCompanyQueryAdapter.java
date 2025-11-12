package com.delivery_signal.eureka.client.company.infrastructure.adapter.out;

import com.delivery_signal.eureka.client.company.application.port.out.OrderCompanyQueryPort;
import com.delivery_signal.eureka.client.company.application.result.OrderCompanyResult;
import com.delivery_signal.eureka.client.company.domain.entity.Company;
import com.delivery_signal.eureka.client.company.domain.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OrderCompanyQueryAdapter implements OrderCompanyQueryPort {

    private final CompanyRepository companyRepository;

    @Override
    public OrderCompanyResult findCompanyById(UUID companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found: " + companyId));

        return OrderCompanyResult.builder()
                .companyId(company.getCompanyId())
                .hubId(company.getHubId())
                .address(company.getAddress())
                .build();
    }
}