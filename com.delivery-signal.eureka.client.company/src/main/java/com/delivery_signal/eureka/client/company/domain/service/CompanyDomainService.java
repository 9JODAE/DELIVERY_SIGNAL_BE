package com.delivery_signal.eureka.client.company.domain.service;

import com.delivery_signal.eureka.client.company.domain.entity.Company;
import com.delivery_signal.eureka.client.company.domain.entity.CompanyType;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CompanyDomainService {

    public Company createCompany(String name, UUID hubId, String address, CompanyType type, Long createdBy) {
        return Company.builder()
                .companyName(name)
                .hubId(hubId)
                .address(address)
                .companyType(type)
                .createdBy(createdBy)
                .build();
    }
}