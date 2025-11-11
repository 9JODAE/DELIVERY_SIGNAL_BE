package com.delivery_signal.eureka.client.order.presentation.internal.controller;

import com.delivery_signal.eureka.client.order.application.service.internal.InternalOrderService;
import com.delivery_signal.eureka.client.order.application.dto.response.OrderForDeliveryResponseDto;
import com.delivery_signal.eureka.client.order.presentation.internal.dto.response.OrderPongResponseDto;
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
    public ResponseEntity<OrderPongResponseDto> ping(
            @RequestParam(required = false) String from // optional
    ) {
        log.info("Ping received : {}", from != null ? from : "unknown");

        OrderPongResponseDto response = new OrderPongResponseDto(
                "order-service 통신이 제대로 되고 있습니다!",
                "OK",
                Instant.now()
        );
        return ResponseEntity.ok(response);
    }

    // TEST 엔드포인트
    @Operation(summary = "test", description = "배송용 test")
    @GetMapping
    public ResponseEntity<String> test() {
        return ResponseEntity.status(HttpStatus.OK).body("Delivery Service 통신 성공");
    }

    @Operation(
            summary = "배송 서비스용 주문 조회",
            description = """
                배송 서비스가 주문 정보를 가져갈 때 사용하는 API입니다.
                게이트웨이를 통해 접근할 경우 URL은 다음과 같습니다:

                **GET /api/v1/orders/external/{order-id}**
                """)
    @GetMapping("/{order-id}")
    public ResponseEntity<OrderForDeliveryResponseDto> getOrderForDelivery(@PathVariable("order-id") UUID orderId) {
        log.info("배송 서비스에서 주문 조회 요청: {}", orderId);

        OrderForDeliveryResponseDto response = internalOrderService.getOrderForDelivery(orderId); // Application Layer 호출

        return ResponseEntity.ok(response);
    }

}
