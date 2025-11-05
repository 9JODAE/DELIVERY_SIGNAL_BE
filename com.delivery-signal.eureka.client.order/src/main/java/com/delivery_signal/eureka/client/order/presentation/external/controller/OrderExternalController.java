package com.delivery_signal.eureka.client.order.presentation.external.controller;


import com.delivery_signal.eureka.client.order.domain.entity.Order;
import com.delivery_signal.eureka.client.order.domain.repository.OrderRepository;
import com.delivery_signal.eureka.client.order.presentation.external.dto.response.OrderForDeliveryResponse;
import com.delivery_signal.eureka.client.order.presentation.external.dto.response.OrderPongResponseDto;
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
@RequestMapping("/v1/orders/external")
@Tag(name = "외부 통신용 API", description = "외부 통신용 API")
@RequiredArgsConstructor
public class OrderExternalController {

    private final OrderRepository orderRepository;

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

    @Operation(summary = "배송 서비스용 주문 조회", description = "배송 서비스가 주문 정보를 가져갈 때 사용하는 API")
    @GetMapping("/{order-id}")
    public ResponseEntity<OrderForDeliveryResponse> getOrderForDelivery(@PathVariable("order-id") UUID orderId) {
        log.info("배송 서비스에서 주문 조회 요청: {}", orderId);

        Order order = orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다: " + orderId));

        OrderForDeliveryResponse response = new OrderForDeliveryResponse(
                order.getId(),
                order.getSupplierCompanyId(),
                order.getReceiverCompanyId(),
                order.getDeliveryId(),
                order.getRequestNote()
        );

        return ResponseEntity.ok(response);
    }

}
