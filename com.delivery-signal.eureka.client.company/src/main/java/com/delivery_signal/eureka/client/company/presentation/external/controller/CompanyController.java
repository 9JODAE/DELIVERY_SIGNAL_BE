package com.delivery_signal.eureka.client.company.presentation.external.controller;

import com.delivery_signal.eureka.client.company.application.command.*;
import com.delivery_signal.eureka.client.company.application.result.*;
import com.delivery_signal.eureka.client.company.application.service.CompanyService;
import com.delivery_signal.eureka.client.company.presentation.external.dto.request.*;
import com.delivery_signal.eureka.client.company.presentation.external.dto.response.*;
import com.delivery_signal.eureka.client.company.presentation.external.mapper.command.*;
import com.delivery_signal.eureka.client.company.presentation.external.mapper.response.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/companies")
@Tag(name = "Company API", description = "업체 관련 API")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @Operation(summary = "업체 등록", description = "신규 업체를 등록합니다.")
    @PostMapping
    public ResponseEntity<CompanyCreateResponseDto> createCompany(
            @RequestBody CompanyCreateRequestDto request) {
        CreateCompanyCommand command = CompanyCreateMapper.toCommand(request);
        CompanyCreateResult result = companyService.createCompany(command);
        CompanyCreateResponseDto response = CompanyResponseMapper.toCreateResponse(result);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "업체 단건 조회", description = "업체 상세 정보를 조회합니다.")
    @GetMapping("/{companyId}")
    public ResponseEntity<CompanyDetailResponseDto> getCompanyById(
            @PathVariable UUID companyId) {
        CompanyDetailResult result = companyService.getCompanyById(companyId);
        CompanyDetailResponseDto response = CompanyResponseMapper.toDetailResponse(result);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "전체 업체 조회", description = "등록된 모든 업체를 조회합니다.")
    @GetMapping
    public ResponseEntity<List<CompanyListResponseDto>> getAllCompanies() {
        List<CompanyListResult> results = companyService.getAllCompanies();
        List<CompanyListResponseDto> response = results.stream()
                .map(CompanyResponseMapper::toListResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "특정 허브 소속 업체 조회", description = "특정 허브에 속한 업체를 조회합니다.")
    @GetMapping("/hub/{hubId}")
    public ResponseEntity<List<CompanyListResponseDto>> getCompaniesByHub(
            @PathVariable UUID hubId) {
        List<CompanyListResult> results = companyService.getCompaniesByHub(hubId);
        List<CompanyListResponseDto> response = results.stream()
                .map(CompanyResponseMapper::toListResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "업체 수정", description = "업체 정보를 수정합니다.")
    @PutMapping("/{companyId}")
    public ResponseEntity<CompanyUpdateResponseDto> updateCompany(
            @PathVariable UUID companyId,
            @RequestBody CompanyUpdateRequestDto request) {

        UpdateCompanyCommand command = CompanyUpdateMapper.toCommand(companyId, request);
        CompanyUpdateResult result = companyService.updateCompany(command);
        CompanyUpdateResponseDto response = CompanyResponseMapper.toUpdateResponse(result);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "업체 삭제", description = "업체를 삭제합니다.")
    @DeleteMapping("/{companyId}")
    public ResponseEntity<CompanyDeleteResponseDto> deleteCompany(@PathVariable UUID companyId) {
        DeleteCompanyCommand command = CompanyDeleteMapper.toCommand(companyId);
        CompanyDeleteResult result = companyService.deleteCompany(command);
        CompanyDeleteResponseDto response = CompanyResponseMapper.toDeleteResponse(result);
        return ResponseEntity.ok(response);
    }
}
