package com.delivery_signal.eureka.client.delivery.application.service;

import com.delivery_signal.eureka.client.delivery.application.command.CreateDeliveryCommand;
import com.delivery_signal.eureka.client.delivery.application.command.UpdateDeliveryStatusCommand;
import com.delivery_signal.eureka.client.delivery.application.dto.DeliveryListQuery;
import com.delivery_signal.eureka.client.delivery.application.dto.DeliveryQueryResponse;
import com.delivery_signal.eureka.client.delivery.application.mapper.DeliveryDomainMapper;
import com.delivery_signal.eureka.client.delivery.domain.model.DeliveryRouteRecords;
import com.delivery_signal.eureka.client.delivery.domain.model.DeliveryStatus;
import com.delivery_signal.eureka.client.delivery.domain.repository.DeliveryRouteRecordsRepository;
import com.delivery_signal.eureka.client.delivery.application.dto.PagedDeliveryResponse;
import com.delivery_signal.eureka.client.delivery.common.UserRole;
import com.delivery_signal.eureka.client.delivery.domain.model.Delivery;
import com.delivery_signal.eureka.client.delivery.domain.repository.DeliveryRepository;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryRouteRecordsRepository deliveryRouteRecordsRepository;
    private final OrderServiceClient orderServiceClient;
    private final DeliveryDomainMapper deliveryDomainMapper;

    public DeliveryService(DeliveryRepository deliveryRepository,
        DeliveryRouteRecordsRepository deliveryRouteRecordsRepository, OrderServiceClient orderServiceClient,
        DeliveryDomainMapper deliveryDomainMapper) {
        this.deliveryRepository = deliveryRepository;
        this.deliveryRouteRecordsRepository = deliveryRouteRecordsRepository;
        this.orderServiceClient = orderServiceClient;
        this.deliveryDomainMapper = deliveryDomainMapper;
    }

    /**
     * Order Service에서 Delivery Service의 API를 호출하여 내부적으로 자동 생성
     * 새로운 주문에 대한 배송 및 전체 경로 기록 생성
     */
    @Transactional
    public DeliveryQueryResponse createDelivery(CreateDeliveryCommand command, Long creatorId) {
        // TODO: 허브 유효성 검사 (Hub 존재 여부 등) - CLIENT 통신 필요
        // TODO: 배송 경로 기록은 추후에 추가 예정
        Delivery delivery = Delivery.create(command.orderId(),
            command.companyId(),
            command.status(),
            command.departureHubId(),
            command.destinationHubId(),
            command.address(),
            command.recipient(),
            command.recipientSlackId(),
            command.deliveryManagerId(),
            creatorId
        );
        Delivery savedDelivery = deliveryRepository.save(delivery);

        // 배송(허브 이동) 경로 기록 엔티티 목록 생성 및 저장
        // TODO: 배송 담당자 할당 로직 수정 필요
        Long initialHubManagerId = assignInitialHubManager();

        List<DeliveryRouteRecords> routeRecords = command.routes().stream()
            .map(segment ->
                DeliveryRouteRecords.initialCreate(
                    delivery,
                    segment.sequence(),
                    segment.departureHubId(),
                    segment.destinationHubId(),
                    segment.estDistance(),
                    segment.estTime(),
                    initialHubManagerId,
                    creatorId
                ))
            .toList();

        // (허브 이동 정보) 경로 기록 리스트 저장
        deliveryRouteRecordsRepository.saveAll(routeRecords);
        return deliveryDomainMapper.toResponse(savedDelivery);
    }

    // TODO: 테스트용, 추후 삭제 예정
    // DeliveryService의 헬스체크나 특정 로직에서 OrderService의 상태를 확인할 때 사용
//    public OrderServiceClient.OrderPongResponseDto checkOrderServiceStatus() {
//        try {
//            // FeignClient를 호출합니다. 'from' 파라미터에 호출자(DeliveryService) 명시
//            return orderServiceClient.ping("안녕안녕안녕");
//        } catch (Exception e) {
//            // 통신 실패 처리 (여기서 Resilience4j가 동작합니다)
//            // 실제 MSA에서는 이 통신이 실패해도 시스템이 멈추지 않도록 설계해야 합니다.
//            // ... 로그 기록 및 Fallback 처리
//            return new OrderServiceClient.OrderPongResponseDto(
//                "Order-service 통신 실패",
//                "ERROR",
//                Instant.now()
//            );
//        }
//    }

    /**
     * 담당 배송 목록 조회
     * 권한: 배송 담당자 본인만 가능
     */
    @Transactional(readOnly = true)
    public PagedDeliveryResponse getMyDeliveries(Long currUserId, String role, DeliveryListQuery query) {

        // 권한 확인 : 배송 담당자는 자신이 담당하는 배송만 조회 가능
        if (!Objects.equals(role, UserRole.DELIVERY_MANAGER.name())) {
            throw new RuntimeException("담당 배송 목록을 조회할 권한이 없습니다. (ROLE: " + role + ")");
        }

        Sort sort = Sort.by(query.direction(), query.sortBy());
        Pageable pageable = PageRequest.of(query.page(), query.size(), sort);

        // Domain 객체의 Page 반환
        Page<Delivery> deliveryPage = deliveryRepository.findActivePageByManagerId(currUserId,
            pageable);

        List<DeliveryQueryResponse> responses = deliveryPage.getContent()
            .stream()
            .map(deliveryDomainMapper::toResponse)
            .toList();

        return PagedDeliveryResponse.from(deliveryPage, responses);
    }

    @Transactional
    public DeliveryQueryResponse updateDeliveryStatus(UUID deliveryId, UpdateDeliveryStatusCommand command, Long updatorId, String role) {
        Delivery delivery = getDelivery(deliveryId);
        DeliveryStatus newStatus = DeliveryStatus.valueOf(command.newStatus());

        // 수정 권한: 마스터, 해당 허브 관리자, 해당 배송 담당자만 가능
        if (!hasUpdatePermission(delivery, updatorId, UserRole.valueOf(role))) {
            throw new RuntimeException("배송 상태를 수정할 권한이 없습니다. (ROLE: " + role + ")");
        }

        delivery.updateStatus(newStatus, updatorId);

        // TODO : 비즈니스 로직 (상태 변경에 따른 추가 작업 필요 시)
        // TODO (확인 필요) : PARTNER_MOVING -> COMPLETED로 변경 시, 주문 서비스에 최종 완료 알림 전송 (AI/SLACK 서비스 feign client)
        return deliveryDomainMapper.toResponse(delivery);
    }

    // TODO: Hub 배송 담당자를 할당하는 가상의 로직
    private Long assignInitialHubManager() {
        // TODO:  배송 순번 기준 할당 로직 호출
        return 3L; // 임시 값
    }

    private Delivery getDelivery(UUID deliveryId) {
        return deliveryRepository.findActiveById(deliveryId)
            .orElseThrow(() -> new NoSuchElementException("배송 정보를 찾을 수 없습니다."));
    }

    private boolean hasUpdatePermission(Delivery delivery, Long updatorId, UserRole role) {
        // 마스터 관리자, 해당 허브 관리자, 해당 배송 담당자만 가능
        if (role.equals(UserRole.MASTER)) {
            return true;
        }

        // TODO: 허브 관리자는 delivery의 fromHubId/toHubId 중 하나를 관리하는지 확인 (허브 FeignClient 필요)
        if (role.equals(UserRole.HUB_MANAGER)) {
            // return hubService.isManagingHub(currUserId, delivery.getFromHubId())
            //         || hubService.isManagingHub(currUserId, delivery.getToHubId());
            return true; // 임시 허용
        }

        if (role.equals(UserRole.DELIVERY_MANAGER)) {
            return delivery.getDeliveryManagerId().equals(updatorId);
        }

        return false;
    }
}
