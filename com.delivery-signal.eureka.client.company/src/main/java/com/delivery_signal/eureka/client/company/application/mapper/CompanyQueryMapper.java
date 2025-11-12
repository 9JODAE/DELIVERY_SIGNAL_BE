package com.delivery_signal.eureka.client.company.application.mapper;

import com.delivery_signal.eureka.client.company.application.result.CompanyDetailResult;
import com.delivery_signal.eureka.client.company.application.result.CompanyListResult;
import com.delivery_signal.eureka.client.company.domain.entity.Company;
import com.delivery_signal.eureka.client.company.domain.vo.company.CompanyInfo;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CompanyQueryMapper {

    // Entity -> List DTO (서비스 내부 응답용)
    public CompanyListResult toListDto(Company company) {
        return CompanyListResult.builder()
                .companyId(company.getCompanyId())
                .companyName(company.getCompanyName())
                .type(company.getCompanyType())
                .hubId(company.getHubId())
                .build();
    }

    public List<CompanyListResult> toListDtos(List<Company> companies) {
        return companies.stream()
                .map(this::toListDto)
                .toList();
    }

    // Entity -> Detail DTO (서비스 내부 응답용)
    public CompanyDetailResult toDetailDto(Company company) {
        return CompanyDetailResult.builder()
                .companyId(company.getCompanyId())
                .companyName(company.getCompanyName())
                .type(company.getCompanyType())
                .hubId(company.getHubId())
                .address(company.getAddress())
                .createdAt(company.getCreatedAt())
                .updatedAt(company.getUpdatedAt())
                .build();
    }

    // Entity -> CompanyInfo VO (다른 MSA(예: Order)에서 사용할 경량 VO)
    public CompanyInfo toCompanyInfo(Company company) {
        return CompanyInfo.builder()
                .companyId(company.getCompanyId())
                .companyName(company.getCompanyName())
                .type(company.getCompanyType())
                .hubId(company.getHubId())
                .address(company.getAddress())
                .build();
    }

    // 경우에 따라 엔티티가 아닌 DB 레코드(쿼리 결과 DTO)를 매핑해야하면 그 타입도 추가
}
