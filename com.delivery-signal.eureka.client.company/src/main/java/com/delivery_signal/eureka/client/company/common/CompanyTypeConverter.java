package com.delivery_signal.eureka.client.company.common;


import com.delivery_signal.eureka.client.company.domain.entity.CompanyType;
import com.delivery_signal.eureka.client.company.presentation.external.dto.CompanyTypeDto;

public class CompanyTypeConverter {

    public static CompanyTypeDto toDto(CompanyType domainType) {
        if (domainType == null) return null;
        return CompanyTypeDto.valueOf(domainType.name());
    }

    public static CompanyType toDomain(CompanyTypeDto dtoType) {
        if (dtoType == null) return null;
        return CompanyType.valueOf(dtoType.name());
    }
}
