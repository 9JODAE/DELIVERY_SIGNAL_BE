package com.delivery_signal.eureka.client.delivery.presentation.controller.external;

import com.delivery_signal.eureka.client.delivery.application.command.CreateDeliveryManagerCommand;
import com.delivery_signal.eureka.client.delivery.application.command.UpdateManagerCommand;
import com.delivery_signal.eureka.client.delivery.application.dto.ManagerQueryResponse;
import com.delivery_signal.eureka.client.delivery.application.service.DeliveryManagerService;
import com.delivery_signal.eureka.client.delivery.presentation.dto.ApiResponse;
import com.delivery_signal.eureka.client.delivery.presentation.dto.request.DeliveryManagerRegisterRequest;
import com.delivery_signal.eureka.client.delivery.presentation.dto.response.DeliveryManagerResponse;
import com.delivery_signal.eureka.client.delivery.presentation.mapper.DeliveryManagerMapper;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/deliveries/managers")
public class DeliveryManagerController {

    private final DeliveryManagerService deliveryManagerService;
    private final DeliveryManagerMapper deliveryManagerMapper; // ResponseDTO 매퍼

    // API Gateway에서 인증 후, USER ID와 ROLE을 헤더에 담아 전달
    private static final String USER_ID_HEADER = "X-User-Id";
    private static final String USER_ROLE_HEADER = "X-User-Role";
    // 허브 관리자의 담당 허브 ID
    private static final String USER_HUB_ID_HEADER = "X-User-Hub-Id";

    public DeliveryManagerController(DeliveryManagerService deliveryManagerService,
        DeliveryManagerMapper deliveryManagerMapper) {
        this.deliveryManagerService = deliveryManagerService;
        this.deliveryManagerMapper = deliveryManagerMapper;
    }

    /**
     * 배송 생성 (권한: MASTER, HUB_MANAGER)
     * order-service에서 통신하여 자동 생성
     * userHubId는 role이 마스터 관리자일 경우 NULL, 허브 관리자일 경우에는 ID를 가짐
     */
    @PostMapping
    public ResponseEntity<ApiResponse<DeliveryManagerResponse>> registerManager(
        @Valid @RequestBody DeliveryManagerRegisterRequest request,
        @RequestHeader(USER_ID_HEADER) Long currUserId,
        @RequestHeader(USER_ROLE_HEADER) String role,
        @RequestHeader(value = USER_HUB_ID_HEADER, required = false) UUID userHubId
    ) {
        // Presentation DTO를 Application 커맨드 변환
        CreateDeliveryManagerCommand command = CreateDeliveryManagerCommand.builder()
            .managerId(request.managerId())
            .slackId(request.slackId())
            .type(request.type())
            .hubId(request.hubId())
            .build();
        // Service 호출 및 Application 쿼리 리스폰스 반환
        ManagerQueryResponse response = deliveryManagerService.registerManager(currUserId,
            command);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(deliveryManagerMapper.toResponse(response)));
    }

    /**
     * 배송 담당자 정보 조회 (단, 배송 담당자는 본인의 정보만 조회 가능)
     */
    @GetMapping("/{user-id}")
    public ResponseEntity<ApiResponse<DeliveryManagerResponse>> getDeliveryManager(
        @PathVariable("user-id") Long managerId,
        @RequestHeader(USER_ID_HEADER) Long currUserId,
        @RequestHeader(USER_ROLE_HEADER) String role
    ) {
        ManagerQueryResponse response = deliveryManagerService.getDeliveryManagerInfo(managerId,
            currUserId);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(deliveryManagerMapper.toResponse(response)));
    }

    /**
     * 배송 담당자 수정
     */
    @PatchMapping("/{user-id}")
    public ResponseEntity<ApiResponse<DeliveryManagerResponse>> updateManager(
        @PathVariable("user-id") Long managerId,
        @Valid @RequestBody DeliveryManagerRegisterRequest request,
        @RequestHeader(USER_ID_HEADER) Long currUserId,
        @RequestHeader(USER_ROLE_HEADER) String role
    ) {
        UpdateManagerCommand command = UpdateManagerCommand.builder()
            .managerId(request.managerId())
            .slackId(request.slackId())
            .type(request.type())
            .hubId(request.hubId())
            .build();

        ManagerQueryResponse response = deliveryManagerService.updateManager(managerId, command,
            currUserId, role);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(deliveryManagerMapper.toResponse(response)));
    }

    @DeleteMapping("/{user-id}")
    public ResponseEntity<ApiResponse<Void>> deleteManager(
        @PathVariable("user-id") Long managerId,
        @RequestHeader(USER_ID_HEADER) Long currUserId,
        @RequestHeader(USER_ROLE_HEADER) String role
    ) {
        deliveryManagerService.softDeleteManager(managerId, currUserId);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(null));
    }

    /**
     * 다음 배송 담당자 배정
     */
    @PostMapping("/assign")
    public ResponseEntity<ApiResponse<Long>> assignNextManager(
        @RequestHeader(USER_ID_HEADER) Long currUserId,
        @RequestHeader(USER_ROLE_HEADER) String role
    ) {
        Long deliveryManagerId = deliveryManagerService.assignNextDeliveryManager(currUserId);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(deliveryManagerId));
    }
}
