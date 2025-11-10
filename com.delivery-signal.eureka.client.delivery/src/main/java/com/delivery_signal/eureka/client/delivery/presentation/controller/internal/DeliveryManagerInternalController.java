package com.delivery_signal.eureka.client.delivery.presentation.controller.internal;

import com.delivery_signal.eureka.client.delivery.application.service.DeliveryManagerService;
import com.delivery_signal.eureka.client.delivery.presentation.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/open-api/v1/managers")
public class DeliveryManagerInternalController {

    private final DeliveryManagerService deliveryManagerService;

    // API Gateway에서 인증 후, USER ID와 ROLE을 헤더에 담아 전달
    private static final String USER_ID_HEADER = "X-User-Id";
    private static final String USER_ROLE_HEADER = "X-User-Role";
    // 허브 관리자의 담당 허브 ID
    private static final String USER_HUB_ID_HEADER = "X-User-Hub-Id";

    public DeliveryManagerInternalController(DeliveryManagerService deliveryManagerService) {
        this.deliveryManagerService = deliveryManagerService;
    }

    /**
     * 다음 배송 담당자 배정
     * 다른 MSA에서 FeignClient를 통해 호출될 엔드포인트 (내부 호출)
     */
    @PostMapping("/assign")
    public ResponseEntity<ApiResponse<Long>> assignNextManager(
        @RequestHeader(USER_ID_HEADER) Long currUserId,
        @RequestHeader(USER_ROLE_HEADER) String role
    ) {
        Long deliveryManagerId = deliveryManagerService.assignNextDeliveryManager();
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(deliveryManagerId));
    }
}
