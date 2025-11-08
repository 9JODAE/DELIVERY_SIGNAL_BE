package com.delivery_signal.eureka.client.delivery.application.service;

import com.delivery_signal.eureka.client.delivery.application.command.CreateDeliveryCommand;
import com.delivery_signal.eureka.client.delivery.application.dto.DeliveryListQuery;
import com.delivery_signal.eureka.client.delivery.application.dto.DeliveryQueryResponse;
import com.delivery_signal.eureka.client.delivery.domain.model.DeliveryRouteRecords;
import com.delivery_signal.eureka.client.delivery.domain.repository.DeliveryRouteRecordsRepository;
import com.delivery_signal.eureka.client.delivery.presentation.dto.response.PagedDeliveryResponse;
import com.delivery_signal.eureka.client.delivery.common.UserRole;
import com.delivery_signal.eureka.client.delivery.domain.model.Delivery;
import com.delivery_signal.eureka.client.delivery.domain.repository.DeliveryRepository;
import com.delivery_signal.eureka.client.delivery.presentation.mapper.DeliveryMapper;
import java.util.List;
import java.util.Objects;
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
    private final DeliveryMapper deliveryMapper;

    public DeliveryService(DeliveryRepository deliveryRepository,
        DeliveryRouteRecordsRepository deliveryRouteRecordsRepository, OrderServiceClient orderServiceClient,
        DeliveryMapper deliveryMapper) {
        this.deliveryRepository = deliveryRepository;
        this.deliveryRouteRecordsRepository = deliveryRouteRecordsRepository;
        this.orderServiceClient = orderServiceClient;
        this.deliveryMapper = deliveryMapper;
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
                    segment,
                    initialHubManagerId,
                    creatorId
                ))
            .toList();

        // (허브 이동 정보) 경로 기록 리스트 저장
        deliveryRouteRecordsRepository.saveAll(routeRecords);
        return deliveryMapper.toResponse(savedDelivery);
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
            .map(deliveryMapper::toResponse)
            .toList();

        return PagedDeliveryResponse.from(deliveryPage, responses);
    }

    // TODO: Hub 배송 담당자를 할당하는 가상의 로직
    private Long assignInitialHubManager() {
        // TODO:  배송 순번 기준 할당 로직 호출
        return 3L; // 임시 값
    }
}
