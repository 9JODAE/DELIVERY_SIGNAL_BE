package com.delivery_signal.eureka.client.company.domain.repository;

import com.delivery_signal.eureka.client.company.domain.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository //명시적
public interface CompanyRepository extends JpaRepository<Company, UUID>, CompanyRepositoryCustom {

}
