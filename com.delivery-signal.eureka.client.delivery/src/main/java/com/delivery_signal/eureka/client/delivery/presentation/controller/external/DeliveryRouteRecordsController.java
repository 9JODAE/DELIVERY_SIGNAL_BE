package com.delivery_signal.eureka.client.delivery.presentation.controller.external;

import com.delivery_signal.eureka.client.delivery.application.command.UpdateRouteRecordCommand;
import com.delivery_signal.eureka.client.delivery.application.dto.RouteRecordQueryResponse;
import com.delivery_signal.eureka.client.delivery.application.service.DeliveryService;
import com.delivery_signal.eureka.client.delivery.presentation.dto.ApiResponse;
import com.delivery_signal.eureka.client.delivery.presentation.dto.request.RouteRecordUpdateRequest;
import com.delivery_signal.eureka.client.delivery.presentation.mapper.DeliveryPresentationMapper;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/open-api/v1/delivery-route-records")
public class DeliveryRouteRecordsController {
    private final DeliveryService deliveryService;
    private final DeliveryPresentationMapper deliveryPresentationMapper;

    // API Gateway에서 인증 후, USER ID와 ROLE을 헤더에 담아 전달
    private static final String USER_ID_HEADER = "X-User-Id";
    private static final String USER_ROLE_HEADER = "X-User-Role";
    // 담당 허브 ID
    private static final String USER_HUB_ID_HEADER = "X-User-Hub-Id";

    public DeliveryRouteRecordsController(DeliveryService deliveryService,
        DeliveryPresentationMapper deliveryPresentationMapper) {
        this.deliveryService = deliveryService;
        this.deliveryPresentationMapper = deliveryPresentationMapper;
    }

    /**
     * 허브 간 이동 경로 상태 및 실제 정보 기록 (업데이트 방식)
     * => 추후 점이력 형식으로 쌓는 방식도 고려 (출발지 허브 ID와 목적지 허브 ID가 가지는 식별성을 통해 조회할 수 있게끔)
     * 예: HUB_WAITING -> HUB_MOVING, HUB_MOVING -> HUB_ARRIVED
     */
    @PatchMapping("/{route-id}")
    public ResponseEntity<ApiResponse<RouteRecordQueryResponse>> recordHubMovement(
        @PathVariable("route-id") UUID routeId,
        @Valid @RequestBody RouteRecordUpdateRequest request,
        @RequestHeader(USER_ID_HEADER) Long currUserId,
        @RequestHeader(USER_ROLE_HEADER) String role
    ) {
        UpdateRouteRecordCommand command = deliveryPresentationMapper.toUpdateRouteRecordCommand(
            request);
        RouteRecordQueryResponse response = deliveryService.recordHubMovement(routeId,
            command, currUserId, role);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(response));
    }
}
