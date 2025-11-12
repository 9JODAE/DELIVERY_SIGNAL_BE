package com.delivery_signal.eureka.client.company.infrastructure.adapter.out;


import com.delivery_signal.eureka.client.company.application.mapper.CompanyQueryMapper;
import com.delivery_signal.eureka.client.company.application.port.out.CompanyQueryPort;
import com.delivery_signal.eureka.client.company.application.result.CompanyDetailResult;
import com.delivery_signal.eureka.client.company.application.result.CompanyListResult;
import com.delivery_signal.eureka.client.company.domain.entity.Company;
import com.delivery_signal.eureka.client.company.domain.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CompanyQueryAdapter implements CompanyQueryPort {

    private final CompanyRepository repository;


    private final CompanyRepository companyRepository;
    private final CompanyQueryMapper companyQueryMapper;

    @Override
    public CompanyDetailResult getCompanyById(UUID companyId) {
        return null;
    }

    @Override
    public List<CompanyListResult> getAllCompanies() {
        List<Company> companies = companyRepository.findAll();// 예시 메서드
        return companyQueryMapper.toListDtos(companies);
    }

    @Override
    public List<CompanyListResult> getCompaniesByHub(UUID hubId) {
        List<Company> companies = companyRepository.findByHubIdAndDeletedAtIsNull(hubId);
        return companyQueryMapper.toListDtos(companies);
    }

}
