package com.delivery_signal.eureka.client.company.infrastructure.adapter.out;

import com.delivery_signal.eureka.client.company.application.command.CreateCompanyCommand;
import com.delivery_signal.eureka.client.company.application.command.DeleteCompanyCommand;
import com.delivery_signal.eureka.client.company.application.command.UpdateCompanyCommand;
import com.delivery_signal.eureka.client.company.application.port.out.CompanyCommandPort;
import com.delivery_signal.eureka.client.company.application.result.CompanyCreateResult;
import com.delivery_signal.eureka.client.company.application.result.CompanyDeleteResult;
import com.delivery_signal.eureka.client.company.application.result.CompanyUpdateResult;
import com.delivery_signal.eureka.client.company.domain.entity.Company;
import com.delivery_signal.eureka.client.company.domain.repository.CompanyRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.UUID;

@Component
public class CompanyCommandAdapter implements CompanyCommandPort {

    private final CompanyRepository repository;

    public CompanyCommandAdapter(CompanyRepository repository) {
        this.repository = repository;
    }

    @Override
    public CompanyCreateResult createCompany(CreateCompanyCommand command) {
        Company company = Company.builder()
                .id(UUID.randomUUID())
                .hubId(command.getHubId())
                .name(command.getName())
                .type(command.getType())
                .address(command.getAddress())
                .createdAt(LocalDateTime.now())
                .build();

        Company entity = toEntity(company);
        repository.save(entity);

        return toCreateResult(entity);
    }

    @Override
    public CompanyUpdateResult updateCompany(UpdateCompanyCommand command) {
        Company entity = repository.findById(command.getCompanyId())
                .orElseThrow(() -> new IllegalArgumentException("업체를 찾을 수 없습니다."));

        entity.setName(command.getName());
        entity.setType(command.getType());
        entity.setAddress(command.getAddress());
        entity.setUpdatedAt(LocalDateTime.now());

        repository.save(entity);

        return toUpdateResult(entity);
    }

    @Override
    public CompanyDeleteResult deleteCompany(DeleteCompanyCommand command) {
        Company entity = repository.findById(command.getCompanyId())
                .orElseThrow(() -> new IllegalArgumentException("업체를 찾을 수 없습니다."));

        entity.setDeletedAt(LocalDateTime.now());
        entity.setDeletedBy(entity.getDeletedBy());

        repository.save(entity);

        return toDeleteResult(entity);
    }

    private Company toEntity(Company c) {
        return Company.builder()
                .id(c.getId())
                .hubId(c.getHubId())
                .name(c.getName())
                .type(c.getType())
                .address(c.getAddress())
                .createdAt(c.getCreatedAt())
                .updatedAt(c.getUpdatedAt())
                .deletedAt(c.getDeletedAt())
                .deletedBy(c.getDeletedBy())
                .build();
    }

    private CompanyCreateResult toCreateResult(Company e) {
        return CompanyCreateResult.builder()
                .id(e.getId())
                .hubId(e.getHubId())
                .name(e.getName())
                .type(e.getType())
                .address(e.getAddress())
                .createdAt(e.getCreatedAt())
                .build();
    }

    private CompanyUpdateResult toUpdateResult(Company e) {
        return CompanyUpdateResult.builder()
                .id(e.getId())
                .name(e.getName())
                .type(e.getType())
                .address(e.getAddress())
                .updatedAt(e.getUpdatedAt())
                .build();
    }

    private CompanyDeleteResult toDeleteResult(Company e) {
        return CompanyDeleteResult.builder()
                .id(e.getId())
                .deletedAt(e.getDeletedAt())
                .deletedBy(e.getDeletedBy())
                .build();
    }
}
