package com.delivery_signal.eureka.client.company.application.service;

import com.delivery_signal.eureka.client.company.application.command.*;
import com.delivery_signal.eureka.client.company.application.mapper.CompanyQueryMapper;
import com.delivery_signal.eureka.client.company.application.port.out.CompanyQueryPort;
import com.delivery_signal.eureka.client.company.application.port.out.HubQueryPort;
import com.delivery_signal.eureka.client.company.application.port.out.UserQueryPort;
import com.delivery_signal.eureka.client.company.application.result.*;
import com.delivery_signal.eureka.client.company.application.validator.CompanyPermissionValidator;
import com.delivery_signal.eureka.client.company.common.NotFoundException;
import com.delivery_signal.eureka.client.company.domain.entity.Company;
import com.delivery_signal.eureka.client.company.domain.repository.CompanyRepository;
import com.delivery_signal.eureka.client.company.domain.service.CompanyDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    // 외부 MSA 호출용 Port
    private final HubQueryPort hubQueryPort;
    private final UserQueryPort userQueryPort;

    // 도메인 및 검증, 매퍼
    private final CompanyDomainService companyDomainService;
    private final CompanyPermissionValidator companyPermissionValidator;
    private final CompanyQueryMapper companyQueryMapper;
    private final CompanyQueryPort companyQueryPort;


    /** 업체 등록 */
    public CompanyCreateResult createCompany(CreateCompanyCommand command) {

        // 권한 체크
        companyPermissionValidator.validateCreate(command.getUserId(), command.getHubId());

        // 허브 존재 여부 검증
        if (!hubQueryPort.existsByHubId(command.getHubId())) {
            throw new NotFoundException("허브", command.getHubId());
        }

        // 유저 활성 여부 확인
        if (!userQueryPort.isUserApproved(command.getUserId())) {
            throw new NotFoundException("유저", command.getUserId());
        }

        // 도메인 엔티티 생성
        Company company = companyDomainService.createCompany(
                command.getName(),
                command.getHubId(),
                command.getAddress(),
                command.getType(),
                command.getUserId()
        );

        companyRepository.save(company);
        log.info("업체 생성 완료: {}", company.getCompanyName());

        return CompanyCreateResult.builder()
                .companyId(company.getCompanyId())
                .createdBy(company.getCreatedBy())
                .createdAt(company.getCreatedAt())
                .message("업체 등록이 완료되었습니다.")
                .build();
    }


    /** 업체 단건 조회 */
    @Transactional(readOnly = true)
    public CompanyDetailResult getCompanyById(UUID companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new NotFoundException("업체", companyId));
        return companyQueryMapper.toDetailDto(company);
    }


    /** 전체 업체 조회 */
    @Transactional(readOnly = true)
    public List<CompanyListResult> getAllCompanies() {
        List<Company> companies = companyRepository.findAll();
        return companyQueryMapper.toListDtos(companies);
    }


    /** 허브별 업체 조회 */
    @Transactional(readOnly = true)
    public List<CompanyListResult> getCompaniesByHub(UUID hubId) {

        // 허브 검증
        if (!hubQueryPort.existsByHubId(hubId)) {
            throw new NotFoundException("허브", hubId);
        }

        List<Company> companies = companyRepository.findByHubIdAndDeletedAtIsNull(hubId);
        return companyQueryMapper.toListDtos(companies);
    }


    /** 업체 수정 */
    public CompanyUpdateResult updateCompany(UpdateCompanyCommand command) {

        Company company = companyRepository.findById(command.getCompanyId())
                .orElseThrow(() -> new NotFoundException("업체", command.getCompanyId()));

        // 권한 체크
        companyPermissionValidator.validateUpdate(command.getUserId(),company.getHubId(),command.getUserId());

        // 허브 유효성 체크
        if (!hubQueryPort.existsByHubId(company.getHubId())) {
            throw new NotFoundException("허브", company.getHubId());
        }

        company.updateInfo(
                command.getCompanyName(),
                command.getAddress(),
                command.getCompanyType(),
                command.getHubId()
        );

        companyRepository.save(company);

        return CompanyUpdateResult.builder()
                .companyId(company.getCompanyId())
                .updatedBy(company.getUpdatedBy())
                .updatedAt(company.getUpdatedAt())
                .build();
    }


    /** 업체 삭제 */
    public CompanyDeleteResult deleteCompany(DeleteCompanyCommand command) {

        Company company = companyRepository.findById(command.getCompanyId())
                .orElseThrow(() -> new NotFoundException("업체", command.getCompanyId()));

        companyPermissionValidator.validateDelete(command.getUserId(),company.getHubId());

        company.markAsDeleted(LocalDateTime.now());
        companyRepository.save(company);

        log.info("업체 삭제 완료: {}", company.getCompanyName());

        return CompanyDeleteResult.builder()
                .companyId(company.getCompanyId())
                .deletedBy(company.getDeletedBy())
                .deletedAt(company.getDeletedAt())
                .message("업체가 삭제되었습니다.")
                .build();
    }
}
