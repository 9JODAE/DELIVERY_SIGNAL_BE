package com.delivery_signal.eureka.client.order.presentation.internal.controller;

import com.delivery_signal.eureka.client.order.application.service.internal.InternalOrderService;
import com.delivery_signal.eureka.client.order.application.result.OrderForDeliveryResult;
import com.delivery_signal.eureka.client.order.presentation.external.dto.response.ApiResponse;
import com.delivery_signal.eureka.client.order.presentation.internal.dto.response.DeliveryCreateResponseDto;
import com.delivery_signal.eureka.client.order.presentation.internal.dto.response.OrderPongResponseDto;
import com.delivery_signal.eureka.client.order.presentation.internal.mapper.OrderDeliveryResponseMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/open-api/v1/orders")
@Tag(name = "내부 통신용 API", description = """
        게이트웨이 기준 호출 경로 예시:
        - 주문 조회 (배송 서비스용): **GET /open-api/v1/orders/{order-id}**
        """)
@RequiredArgsConstructor
public class OrderInternalController {

    private final InternalOrderService internalOrderService;

    @Operation(summary = "헬스체크", description = "통신 체크")
    @GetMapping("/ping")
    public ResponseEntity<ApiResponse<OrderPongResponseDto>> ping(
            @RequestParam(required = false) String from // optional
    ) {
        log.info("Ping received : {}", from != null ? from : "unknown");

        OrderPongResponseDto response = new OrderPongResponseDto(
                "order-service 통신이 제대로 되고 있습니다!",
                "OK",
                Instant.now()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }

    // TEST 엔드포인트
    @Operation(summary = "test", description = "배송용 test")
    @GetMapping
    public ResponseEntity<ApiResponse<String>> test() {

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("통신 성공"));
    }

    @Operation(
            summary = "외부 서비스용 주문 조회",
            description = """
                외부서비스용 주문 조회입니다.
                게이트웨이를 통해 접근할 경우 URL은 다음과 같습니다:

                **GET /open-api/v1/orders/{order-id}**
                """)
    @GetMapping("/{order-id}")
    public ResponseEntity<ApiResponse<DeliveryCreateResponseDto>> getOrderForDelivery(@PathVariable("order-id") UUID orderId) {
        log.info("배송 서비스에서 주문 조회 요청: {}", orderId);

        OrderForDeliveryResult result = internalOrderService.getOrderForDelivery(orderId); // Application Layer 호출
        DeliveryCreateResponseDto responseDto = OrderDeliveryResponseMapper.toResponse(result);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(responseDto));
    }
}
