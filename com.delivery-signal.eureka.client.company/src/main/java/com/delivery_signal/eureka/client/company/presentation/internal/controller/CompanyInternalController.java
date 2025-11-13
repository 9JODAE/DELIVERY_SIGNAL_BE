package com.delivery_signal.eureka.client.company.presentation.internal.controller;

import com.delivery_signal.eureka.client.company.application.dto.ApiResponse;
import com.delivery_signal.eureka.client.company.application.result.OrderCompanyResult;
import com.delivery_signal.eureka.client.company.application.service.InternalOrderCompanyService;
import com.delivery_signal.eureka.client.company.presentation.internal.dto.response.OrderCompanyResponseDto;
import com.delivery_signal.eureka.client.company.presentation.internal.mapper.response.OrderCompanyResponseMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/open-api/v1/companies")
@Tag(name = "내부 통신용 업체 API", description = """
        게이트웨이 기준 호출 경로 예시:
        - 특정 업체 조회: **GET /open-api/v1/companies/{company-id}**
        """)
@RequiredArgsConstructor
public class CompanyInternalController {

    private final InternalOrderCompanyService internalCompanyService;

    @Operation(summary = "테스트", description = "통신 테스트용 엔드포인트")
    @GetMapping
    public ResponseEntity<String> test() {
        return ResponseEntity.status(HttpStatus.OK).body("Order Service 통신 성공");
    }

    @Operation(
            summary = "특정 업체 조회",
            description = """
                주문 서비스 등에서 업체 정보를 가져갈 때 사용하는 내부 API입니다.
                **GET /open-api/v1/companies/{company-id}**
                """)
    @GetMapping("/{company-id}")
    public ResponseEntity<ApiResponse<OrderCompanyResponseDto>> getCompanyById(@PathVariable("company-id") UUID companyId) {
        log.info("내부 서비스에서 업체 조회 요청: {}", companyId);

        OrderCompanyResult result = internalCompanyService.getCompanyInfo(companyId);
        OrderCompanyResponseDto responseDto = OrderCompanyResponseMapper.toResponse(result);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(responseDto));
    }
}
