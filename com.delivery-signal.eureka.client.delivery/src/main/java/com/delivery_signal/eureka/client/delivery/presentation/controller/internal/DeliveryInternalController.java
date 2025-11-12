package com.delivery_signal.eureka.client.delivery.presentation.controller.internal;

import com.delivery_signal.eureka.client.delivery.application.command.CreateDeliveryCommand;
import com.delivery_signal.eureka.client.delivery.application.dto.DeliveryQueryResponse;
import com.delivery_signal.eureka.client.delivery.application.port.HubPort;
import com.delivery_signal.eureka.client.delivery.application.service.DeliveryService;
import com.delivery_signal.eureka.client.delivery.presentation.dto.ApiResponse;
import com.delivery_signal.eureka.client.delivery.presentation.dto.request.DeliveryCreateRequest;
import com.delivery_signal.eureka.client.delivery.presentation.mapper.DeliveryPresentationMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 부가 설명
 * /open-api/ : 로그인 정보 없이 통신할 때 쓰는 프리픽스
 * /api/ : 통신 시 로그인 인가가 필요할 때 쓰는 프리픽스
 */
@RestController
@RequestMapping("/open-api/v1/deliveries")
public class DeliveryInternalController {

    private final DeliveryService deliveryService;
    private final DeliveryPresentationMapper deliveryPresentationMapper;
    private final HubPort hubPort;

    // API Gateway에서 인증 후, USER ID와 ROLE을 헤더에 담아 전달
    private static final String USER_ID_HEADER = "X-User-Id";
    private static final String USER_ROLE_HEADER = "X-User-Role";
    // 담당 허브 ID
    private static final String USER_HUB_ID_HEADER = "X-User-Hub-Id";

    public DeliveryInternalController(DeliveryService deliveryService, DeliveryPresentationMapper deliveryPresentationMapper,
        HubPort hubPort) {
        this.deliveryService = deliveryService;
        this.deliveryPresentationMapper = deliveryPresentationMapper;
        this.hubPort = hubPort;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<DeliveryQueryResponse>> createDelivery(
        @Valid @RequestBody DeliveryCreateRequest request,
        @RequestHeader(USER_ID_HEADER) Long currUserId,
        @RequestHeader(USER_ROLE_HEADER) String role
    ) {
        // 권한 확인 불필요 (내부 시스템에서 호출되므로)
        CreateDeliveryCommand command = deliveryPresentationMapper.toCreateDeliveryCommand(request);
        DeliveryQueryResponse response = deliveryService.createDelivery(command, currUserId);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }
}
