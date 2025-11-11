package com.delivery_signal.eureka.client.delivery.presentation.controller.external;

import com.delivery_signal.eureka.client.delivery.application.command.CreateDeliveryCommand;
import com.delivery_signal.eureka.client.delivery.application.command.SearchDeliveryCommand;
import com.delivery_signal.eureka.client.delivery.application.command.UpdateDeliveryInfoCommand;
import com.delivery_signal.eureka.client.delivery.application.command.UpdateDeliveryStatusCommand;
import com.delivery_signal.eureka.client.delivery.application.dto.DeliveryListQuery;
import com.delivery_signal.eureka.client.delivery.application.dto.DeliveryQueryResponse;
import com.delivery_signal.eureka.client.delivery.application.dto.RouteRecordQueryResponse;
import com.delivery_signal.eureka.client.delivery.application.service.DeliveryService;
import com.delivery_signal.eureka.client.delivery.domain.entity.DeliveryStatus;
import com.delivery_signal.eureka.client.delivery.presentation.dto.ApiResponse;
import com.delivery_signal.eureka.client.delivery.presentation.dto.request.DeliveryCreateRequest;
import com.delivery_signal.eureka.client.delivery.application.dto.PagedDeliveryResponse;
import com.delivery_signal.eureka.client.delivery.presentation.dto.request.DeliveryInfoUpdateRequest;
import com.delivery_signal.eureka.client.delivery.presentation.dto.request.DeliveryStatusUpdateRequest;
import com.delivery_signal.eureka.client.delivery.presentation.mapper.DeliveryPresentationMapper;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 부가 설명
 * /open-api/ : 로그인 정보 없이 통신할 때 쓰는 프리픽스
 * /api/ : 통신 시 로그인 인가가 필요할 때 쓰는 프리픽스
 *
 * => delivery의 경우 추후에 /api/ 프리픽스로 바꾸어야함 (로그인 정보 가지고 와서 해야할 경우 대비)
 */
@RestController
@RequestMapping("/open-api/v1/deliveries")
public class DeliveryController {

    private final DeliveryService deliveryService;
    private final DeliveryPresentationMapper deliveryPresentationMapper;

    // API Gateway에서 인증 후, USER ID와 ROLE을 헤더에 담아 전달
    private static final String USER_ID_HEADER = "X-User-Id";
    private static final String USER_ROLE_HEADER = "X-User-Role";
    // 담당 허브 ID
    private static final String USER_HUB_ID_HEADER = "X-User-Hub-Id";

    public DeliveryController(DeliveryService deliveryService, DeliveryPresentationMapper deliveryPresentationMapper) {
        this.deliveryService = deliveryService;
        this.deliveryPresentationMapper = deliveryPresentationMapper;
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

    /**
     * 담당 배송 목록 조회 (배송 담당자)
     * GET /api/v1/deliveries/my?page=0&size=10&sortBy=createdAt&sortDirection=DESC
     */
    @GetMapping("/my")
    public ResponseEntity<ApiResponse<PagedDeliveryResponse>> getMyDeliveries(
        @RequestParam(name = "page", defaultValue = "1") int page,
        @RequestParam(name = "size", defaultValue = "10") int size,
        @RequestParam(name = "sort-by", defaultValue = "createdAt") String sortBy,
        @RequestParam(name = "sort-direction", defaultValue = "DESC") Sort.Direction sortDirection,
        @RequestHeader(USER_ID_HEADER) Long currUserId,
        @RequestHeader(USER_ROLE_HEADER) String role
    ) {

        DeliveryListQuery query = DeliveryListQuery.of(page, size, sortBy, sortDirection);
        PagedDeliveryResponse response = deliveryService.getMyDeliveries(currUserId, role, query);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(response));
    }

    @PatchMapping("/{delivery-id}")
    public ResponseEntity<ApiResponse<DeliveryQueryResponse>> updateDeliveryInfo(
        @PathVariable("delivery-id") UUID deliveryId,
        @Valid @RequestBody DeliveryInfoUpdateRequest request,
        @RequestHeader(USER_ID_HEADER) Long currUserId,
        @RequestHeader(USER_ROLE_HEADER) String role
    ) {
        UpdateDeliveryInfoCommand command = deliveryPresentationMapper.toUpdateDeliveryInfoCommand(
            request);
        DeliveryQueryResponse response = deliveryService.updateDeliveryInfo(deliveryId, command,
            currUserId, role);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(response));
    }

    /**
     * 배송 상태 및 경로 기록 상태 업데이트
     */
    @PatchMapping("/{delivery-id}/status")
    public ResponseEntity<ApiResponse<DeliveryQueryResponse>> updateDeliveryStatus(
        @PathVariable("delivery-id") UUID deliveryId,
        @Valid @RequestBody DeliveryStatusUpdateRequest request,
        @RequestHeader(USER_ID_HEADER) Long currUserId,
        @RequestHeader(USER_ROLE_HEADER) String role
    ) {
        UpdateDeliveryStatusCommand command = deliveryPresentationMapper.toUpdateDeliveryStatusCommand(
            request);
        DeliveryQueryResponse response = deliveryService.updateDeliveryStatus(deliveryId,
            command, currUserId, role);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(response));
    }



    /**
     * 배송 삭제(논리적)
     */
    @DeleteMapping("/{delivery-id}")
    public ResponseEntity<ApiResponse<Void>> deleteDelivery(
        @PathVariable("delivery-id") UUID deliveryId,
        @RequestHeader(USER_ID_HEADER) Long currUserId,
        @RequestHeader(USER_ROLE_HEADER) String role
    ) {
        deliveryService.softDeleteDelivery(deliveryId, currUserId, role);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(null));
    }

    /**
     * 특정 배송의 경로 이력 조회
     */
    @GetMapping("/{delivery-id}/routes")
    public ResponseEntity<ApiResponse<List<RouteRecordQueryResponse>>> getDeliveryRoutes(
        @PathVariable("delivery-id") UUID deliveryId,
        @RequestHeader(USER_ID_HEADER) Long currUserId,
        @RequestHeader(USER_ROLE_HEADER) String role
    ) {
        List<RouteRecordQueryResponse> response = deliveryService.getDeliveryRoutes(deliveryId,
            currUserId, role);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(response));
    }

    /**
     * 배송 목록 조회 : 허브, 업체, 배송 담당자, 배송 상태별
     */
    @GetMapping
    public ResponseEntity<ApiResponse<PagedDeliveryResponse>> searchDeliveries(
        @RequestParam(required = false) UUID hubId,
        @RequestParam(required = false) UUID companyId,
        @RequestParam(required = false) Long deliveryManagerId,
        @RequestParam(required = false) DeliveryStatus status,
        @RequestParam(name = "page", defaultValue = "1") int page,
        @RequestParam(name = "size", defaultValue = "10") int size,
        @RequestParam(name = "sort-by", defaultValue = "createdAt") String sortBy,
        @RequestParam(name = "sort-direction", defaultValue = "DESC") Sort.Direction sortDirection,
        @RequestHeader(USER_ID_HEADER) Long currUserId,
        @RequestHeader(USER_ROLE_HEADER) String role
    ) {
        DeliveryListQuery query = DeliveryListQuery.of(page, size, sortBy, sortDirection);
        SearchDeliveryCommand command = SearchDeliveryCommand.of(status, hubId, companyId, deliveryManagerId);
        PagedDeliveryResponse response = deliveryService.searchDeliveries(currUserId, role,
            command, query);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(response));
    }
}