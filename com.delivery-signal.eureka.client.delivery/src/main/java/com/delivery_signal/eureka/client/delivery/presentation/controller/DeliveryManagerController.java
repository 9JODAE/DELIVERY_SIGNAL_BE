package com.delivery_signal.eureka.client.delivery.presentation.controller;

import com.delivery_signal.eureka.client.delivery.application.service.DeliveryManagerService;
import com.delivery_signal.eureka.client.delivery.presentation.dto.request.DeliveryManagerRegisterRequest;
import com.delivery_signal.eureka.client.delivery.presentation.dto.response.DeliveryManagerResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/managers")
public class DeliveryManagerController {

    private final DeliveryManagerService deliveryManagerService;

    // API Gateway에서 인증 후, USER ID와 ROLE을 헤더에 담아 전달
    private static final String USER_ID_HEADER = "X-User-Id";
    private static final String USER_ROLE_HEADER = "X-User-Role";
    // 담당 허브 ID
    private static final String USER_HUB_ID_HEADER = "X-User-Hub-Id";

    public DeliveryManagerController(DeliveryManagerService deliveryManagerService) {
        this.deliveryManagerService = deliveryManagerService;
    }

    @PostMapping
    public ResponseEntity<DeliveryManagerResponse> registerManager(
        @Valid @RequestBody DeliveryManagerRegisterRequest request,
        @RequestHeader(USER_ID_HEADER) Long currUserId,
        @RequestHeader(USER_ROLE_HEADER) String role
    ) {
        // TODO: Role 추후에 ENUM으로 수정
        // TODO: 권한 체크 -> 마스터 관리자 또는 허브 관리자 (담당 허브) 로직 추가 필요

        DeliveryManagerResponse response = deliveryManagerService.registerManager(currUserId,
            request, role);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
