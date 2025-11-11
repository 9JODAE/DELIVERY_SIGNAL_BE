package com.delivery_signal.eureka.client.company.domain.repository;

import com.delivery_signal.eureka.client.company.domain.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.print.Pageable;
import java.util.List;
import java.util.UUID;

public interface CompanyRepository extends JpaRepository<Company, UUID> {
    List<Company> findAllByDeletedAtIsNull(Pageable pageable);
    List<Company> findByHubIdAndDeletedAtIsNull(UUID hubId);
}
