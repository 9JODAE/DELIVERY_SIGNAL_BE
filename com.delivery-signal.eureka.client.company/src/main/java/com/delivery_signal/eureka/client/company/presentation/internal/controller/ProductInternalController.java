package com.delivery_signal.eureka.client.company.presentation.internal.controller;

import com.delivery_signal.eureka.client.company.application.result.OrderProductResult;
import com.delivery_signal.eureka.client.company.application.service.InternalOrderProductService;
import com.delivery_signal.eureka.client.company.presentation.internal.dto.response.OrderProductResponseDto;
import com.delivery_signal.eureka.client.company.presentation.internal.mapper.response.OrderProductResponseMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/open-api/v1/products")
@Tag(name = "내부 통신용 상품 API", description = """
        게이트웨이 기준 호출 경로 예시:
        - 특정 상품 목록 조회: **GET /open-api/v1/products?productIds=uuid1,uuid2,uuid3**
        """)
@RequiredArgsConstructor
public class ProductInternalController {

    private final InternalOrderProductService internalProductService;

    @Operation(summary = "테스트", description = "통신 테스트용 엔드포인트")
    @GetMapping
    public ResponseEntity<String> test() {
        return ResponseEntity.status(HttpStatus.OK).body("Order Service 통신 성공");
    }

    @Operation(
            summary = "특정 상품 목록 조회",
            description = """
                주문 서비스 등에서 상품 정보를 가져갈 때 사용하는 내부 API입니다.
                **GET /open-api/v1/products?productIds=uuid1,uuid2,uuid3**
                """)
    @GetMapping(params = "productIds")
    public List<OrderProductResponseDto> getProducts(@RequestParam List<UUID> productIds) {
        log.info("내부 서비스에서 상품 조회 요청: {}", productIds);

        List<OrderProductResult> results = internalProductService.getProducts(productIds);
        return OrderProductResponseMapper.toResponseList(results);
    }
}
