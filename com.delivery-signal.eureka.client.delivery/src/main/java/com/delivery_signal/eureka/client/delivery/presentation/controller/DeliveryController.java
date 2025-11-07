package com.delivery_signal.eureka.client.delivery.presentation.controller;

import com.delivery_signal.eureka.client.delivery.application.service.DeliveryService;
import com.delivery_signal.eureka.client.delivery.presentation.dto.request.DeliveryCreateRequest;
import com.delivery_signal.eureka.client.delivery.presentation.dto.response.DeliveryCreateResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/open-api/v1/deliveries")
public class DeliveryController {

    private final DeliveryService deliveryService;

    // API Gateway에서 인증 후, USER ID와 ROLE을 헤더에 담아 전달
    private static final String USER_ID_HEADER = "X-User-Id";
    private static final String USER_ROLE_HEADER = "X-User-Role";
    // 담당 허브 ID
    private static final String USER_HUB_ID_HEADER = "X-User-Hub-Id";

    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    // TEST 엔드포인트
    @GetMapping
    public ResponseEntity<String> test() {
        return ResponseEntity.status(HttpStatus.OK).body("Delivery Service 통신 성공");
    }

    @PostMapping
    public ResponseEntity<DeliveryCreateResponse> createDelivery(
        @Valid @RequestBody DeliveryCreateRequest request,
        @RequestHeader(USER_ID_HEADER) Long currUserId,
        @RequestHeader(USER_ROLE_HEADER) String role
    ) {
        DeliveryCreateResponse response = deliveryService.createDelivery(currUserId, request, role);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}