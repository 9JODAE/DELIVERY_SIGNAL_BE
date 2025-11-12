package com.delivery_signal.eureka.client.company.domain.repository;

import com.delivery_signal.eureka.client.company.domain.entity.Company;

import java.util.List;
import java.util.UUID;

public interface CompanyRepositoryCustom {
    List<Company> findAllByDeletedAtIsNull();
    List<Company> findByHubIdAndDeletedAtIsNull(UUID hubId);
}
