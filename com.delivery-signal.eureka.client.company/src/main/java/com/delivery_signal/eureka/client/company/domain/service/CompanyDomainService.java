package com.delivery_signal.eureka.client.company.domain.service;

import com.delivery_signal.eureka.client.company.domain.entity.Company;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CompanyDomainService {

    public Company createCompany(String name, UUID hubId, String address, String type, Long createdBy) {
        return Company.builder()
                .name(name)
                .hubId(hubId)
                .address(address)
                .type(type)
                .createdBy(createdBy)
                .build();
    }
}