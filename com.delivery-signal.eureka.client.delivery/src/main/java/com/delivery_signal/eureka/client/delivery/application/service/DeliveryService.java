package com.delivery_signal.eureka.client.delivery.application.service;

import com.delivery_signal.eureka.client.delivery.application.command.CreateDeliveryCommand;
import com.delivery_signal.eureka.client.delivery.application.command.SearchDeliveryCommand;
import com.delivery_signal.eureka.client.delivery.application.command.UpdateDeliveryInfoCommand;
import com.delivery_signal.eureka.client.delivery.application.command.UpdateDeliveryStatusCommand;
import com.delivery_signal.eureka.client.delivery.application.command.UpdateRouteRecordCommand;
import com.delivery_signal.eureka.client.delivery.application.dto.DeliveryListQuery;
import com.delivery_signal.eureka.client.delivery.application.dto.DeliveryQueryResponse;
import com.delivery_signal.eureka.client.delivery.domain.vo.DeliverySearchCondition;
import com.delivery_signal.eureka.client.delivery.application.dto.RouteRecordQueryResponse;
import com.delivery_signal.eureka.client.delivery.application.mapper.DeliveryDomainMapper;
import com.delivery_signal.eureka.client.delivery.domain.entity.DeliveryRouteRecords;
import com.delivery_signal.eureka.client.delivery.domain.entity.DeliveryStatus;
import com.delivery_signal.eureka.client.delivery.domain.repository.DeliveryQueryRepository;
import com.delivery_signal.eureka.client.delivery.domain.repository.DeliveryRouteRecordsRepository;
import com.delivery_signal.eureka.client.delivery.application.dto.PagedDeliveryResponse;
import com.delivery_signal.eureka.client.delivery.common.UserRole;
import com.delivery_signal.eureka.client.delivery.domain.entity.Delivery;
import com.delivery_signal.eureka.client.delivery.domain.repository.DeliveryRepository;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
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
    private final DeliveryQueryRepository deliveryQueryRepository;
    private final UserServiceClient userServiceClient;
    private final OrderServiceClient orderServiceClient;
    private final DeliveryDomainMapper deliveryDomainMapper;

    public DeliveryService(DeliveryRepository deliveryRepository,
        DeliveryQueryRepository deliveryQueryRepository,
        DeliveryRouteRecordsRepository deliveryRouteRecordsRepository, UserServiceClient userServiceClient,
        OrderServiceClient orderServiceClient,
        DeliveryDomainMapper deliveryDomainMapper) {
        this.deliveryRepository = deliveryRepository;
        this.deliveryRouteRecordsRepository = deliveryRouteRecordsRepository;
        this.deliveryQueryRepository = deliveryQueryRepository;
        this.userServiceClient = userServiceClient;
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
     * 배송 목록 검색 (페이징/정렬 포함)
     * 권한: 모든 로그인 사용자 ('배송 담당자'는 본인 담당 배송만, '허브 담당자'는 본인 담당)
     */
    public PagedDeliveryResponse searchDeliveries(Long currUserId,
        String role, SearchDeliveryCommand command, DeliveryListQuery query) {

        DeliverySearchCondition condition = DeliverySearchCondition.of(
            command.hubId(),
            command.deliveryManagerId(),
            command.companyId(),
            command.status()
        );

        // 권한 확인은 QueryRepository 내부에서 필터링으로 처리
        UserRole userRole = UserRole.valueOf(role);
        DeliverySearchCondition searchCondition = refineSearchCondition(condition, currUserId, userRole);
        PageRequest pageable = query.toPageable();

        Page<Delivery> deliveryPage = deliveryQueryRepository.searchDeliveries(currUserId, searchCondition,
            pageable, userRole);

        List<DeliveryQueryResponse> responses = deliveryPage.getContent()
            .stream()
            .map(deliveryDomainMapper::toResponse)
            .toList();

        return PagedDeliveryResponse.from(deliveryPage, responses);
    }

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


    /**
     * 배송 정보 수정
     * 권한 : 마스터, 허브 관리자(담당 허브), 배송 관리자(담당 배송)
     */
    @Transactional
    public DeliveryQueryResponse updateDeliveryInfo(UUID deliveryId, UpdateDeliveryInfoCommand command, Long updatorId, String role) {
        Delivery delivery = getDelivery(deliveryId);

        // 수정 권한: 마스터, 해당 허브 관리자, 해당 배송 담당자만 가능
        if (!hasUpdatePermission(delivery, updatorId, UserRole.valueOf(role))) {
            throw new RuntimeException("배송 상태를 수정할 권한이 없습니다. (ROLE: " + role + ")");
        }

        delivery.update(command.address(), command.recipient(), command.recipientSlackId(), updatorId);
        return deliveryDomainMapper.toResponse(delivery);
    }

    /**
     * 배송 상태 업데이트
     * 권한 : 마스터, 허브 관리자(담당 허브), 배송 관리자(담당 배송)
     * 실질적으로 배송(Delivery) 엔티티의 상태 수정이 가능한 시점은 마지막 목적지 허브까지 배송이 완료된 시점
     */
    @Transactional
    public DeliveryQueryResponse updateDeliveryStatus(UUID deliveryId, UpdateDeliveryStatusCommand command, Long updatorId, String role) {
        Delivery delivery = getDelivery(deliveryId);
        DeliveryStatus newStatus = DeliveryStatus.valueOf(command.newStatus());

        // 수정 권한: 마스터, 해당 허브 관리자, 해당 배송 담당자만 가능
        if (!hasUpdatePermission(delivery, updatorId, UserRole.valueOf(role))) {
            throw new RuntimeException("배송 상태를 수정할 권한이 없습니다. (ROLE: " + role + ")");
        }

        // 상태 유효성 검사 : 마지막 최종 목적지 허브까지 배송이 완료된 시점(DELIVERING)이 아닐 경우 예외 처리
        if (!newStatus.equals(DeliveryStatus.DELIVERY_COMPLETED)) {
            throw new IllegalArgumentException("허용되지 않은 상태 변경입니다: " + newStatus.name());
        }

        delivery.updateStatus(newStatus, updatorId);

        // TODO (확인 필요) : PARTNER_MOVING -> COMPLETED로 변경 시, 주문 서비스에 최종 완료 알림 전송 (AI/SLACK 서비스 feign client)
        return deliveryDomainMapper.toResponse(delivery);
    }

    /**
     * 허브 간 이동 경로의 상태 및 실제 정보 기록 (허브 관리자/허브 배송 담당자가 호출)
     * 권한 : 마스터, 허브 관리자(담당 허브), 허브 배송 담당자
     * 마지막 최종 허브에 도착했을 경우, Delivery 엔티티의 상태를 DELIVERING으로 변경
     */
    @Transactional
    public RouteRecordQueryResponse recordHubMovement(UUID routeId, UpdateRouteRecordCommand command,
        Long updatorId, String role) {
        DeliveryRouteRecords record = getDeliveryRouteRecords(routeId);

        Delivery delivery = getDelivery(record.getDelivery().getDeliveryId());

        if (!hasHubMovementPermission(record, updatorId, UserRole.valueOf(role))) {
            throw new RuntimeException("해당 허브 이동 정보를 기록/수정할 권한이 없습니다.");
        }

        DeliveryStatus newStatus = DeliveryStatus.valueOf(command.newStatus());

        // 허브 간 이동 경로 상태 기록 (HUB_WAITING, HUB_MOVING 또는 HUB_ARRIVED 상태로만 가능)
        record.update(newStatus, command.actualDistance(),
            command.actualTime(), updatorId);

        // 최초 허브 출발 시 (첫번째 시퀀스, HUB_MOVING) : Delivery 상태를 HUB_MOVING으로 변경
        if (record.getSequence() == 0 && newStatus.equals(DeliveryStatus.HUB_MOVING)) {
            delivery.updateStatus(newStatus, updatorId);
        }

        // 최종 허브 도착 시 (마지막 시퀀스, HUB_ARRIVED) : Delivery 상태는 아직 HUB_MOVING 유지
        // -> 최종 상태 변경(DELIVERING)은 배송 상태 업데이트 API를 통해 업체 배송 담당자가 수행
        if (newStatus.equals(DeliveryStatus.HUB_ARRIVED)) {
            // N+1 방지 쿼리 호출 및 LIMIT 1 적용
            // Pageable 객체 생성: 0번째 페이지의 첫 번째 결과(LIMIT 1)만 요청
            Pageable limitOne = PageRequest.of(0, 1);

            // 해당 경로 기록이 마지막 경로인지 확인

            List<DeliveryRouteRecords> lastRecordList = deliveryRouteRecordsRepository.findLastRouteRecord(
                record.getDelivery().getDeliveryId(),
                limitOne
            );

            Optional<DeliveryRouteRecords> lastRecordOptional = lastRecordList.stream().findFirst();
            lastRecordOptional.ifPresent(lastRecord -> {
                    // 현재 업데이트된 기록의 시퀀스가 마지막 기록의 시퀀스와 같으면 최종 허브에 도착한 것임
                    if (record.getSequence().equals(lastRecord.getSequence())) {
                        delivery.updateStatus(DeliveryStatus.DELIVERING, updatorId);
                    }
            });
        }

        return deliveryDomainMapper.toResponse(record);
    }

    /**
     * 배송 논리적 삭제
     */
    @Transactional
    public void softDeleteDelivery(UUID deliveryId, Long currUserId, String role) {
        Delivery delivery = getDelivery(deliveryId);

        if (delivery.isDeleted()) {
            throw new RuntimeException("이미 삭제된 배송 정보입니다.");
        }

        if (!hasDeletePermission(delivery, currUserId, UserRole.valueOf(role))) {
            throw new RuntimeException("배송 정보를 삭제할 권한이 없습니다. (ROLE: " + role + ")");
        }

        delivery.softDelete(currUserId);
    }


    /**
     * 배송 경로 이력 조회
     * 권한: 모든 로그인 사용자 (단, 허브 관리자와 배송 담당자는 자신이 담당하는 허브/배송만)
     */
    @Transactional(readOnly = true)
    public List<RouteRecordQueryResponse> getDeliveryRoutes(UUID deliveryId, Long currUserId, String role) {
        Delivery delivery = getDelivery(deliveryId);
        // 조회 및 검색 권한: 모든 로그인 사용자 가능, 단 배송 담당자는 자신이 담당하는 배송만 조회 가능
        if (!hasReadPermission(delivery, currUserId, UserRole.valueOf(role))) {
            throw new RuntimeException("해당 배송의 경로 이력을 조회할 권한이 없습니다.");
        }

        // 배송별 경로 이력 리스트 조회 (논리적 삭제되지 않은 데이터만 조회됨)
        List<DeliveryRouteRecords> records = deliveryRouteRecordsRepository.findAllByDeliveryIdOrderBySequence(
            deliveryId);

        return records.stream()
            .map(deliveryDomainMapper::toResponse)
            .collect(Collectors.toList());
    }

    /**
     * 특정 배송 정보 조회
     */
    @Transactional(readOnly = true)
    public DeliveryQueryResponse getDeliveryInfo(UUID deliveryId, Long currUserId, String role) {
        Delivery delivery = getDelivery(deliveryId);

        if (!hasReadPermission(delivery, currUserId, UserRole.valueOf(role))) {
            throw new RuntimeException("해당 배송 정보를 조회할 권한이 없습니다.");
        }

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

    private DeliveryRouteRecords getDeliveryRouteRecords(UUID routeId) {
        return deliveryRouteRecordsRepository.findActiveById(routeId)
            .orElseThrow(() -> new NoSuchElementException("배송 경로 기록을 찾을 수 없습니다."));
    }

    /**
     * 배송 업데이트 권한: 마스터 관리자, 해당 허브 관리자, 해당 배송 담당자만 가능
     */
    private boolean hasUpdatePermission(Delivery delivery, Long updatorId, UserRole role) {
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

    /**
     * 배송 경로 기록 업데이트(이력 추가) 권한 : 마스터, 허브 관리자, 허브 배송 담당자만 가능
     */
    private boolean hasHubMovementPermission(DeliveryRouteRecords record, Long currUserId, UserRole role) {
        if (role == UserRole.MASTER) return true;

        // TODO: 허브 FeignClient 호출을 통해 currUserId가 record.departureHubId 또는 record.destinationHubId를 담당하는지 확인
        if (role.equals(UserRole.HUB_MANAGER)) {
            // return hubService.isManagingHub(currUserId, record.getFromHubId())
            //         || hubService.isManagingHub(currUserId, record.getToHubId());
            return true; // 임시 허용
        }

        if (role == UserRole.DELIVERY_MANAGER) {
            // 배송 담당자일 경우, 해당 경로에 할당된 허브 배송 담당자여야 함
            // 허브 배송 담당자는 해당 경로에 할당된 hubDeliveryManagerId와 일치해야 함
            return record.getHubDeliveryManagerId() != null && record.getHubDeliveryManagerId().equals(currUserId);
        }
        return false;
    }

    /**
     * 배송 삭제 권한: 마스터 관리자, 허브 관리자(담당 허브)
     */
    private boolean hasDeletePermission(Delivery delivery, Long deletorId, UserRole role) {
        if (role.equals(UserRole.MASTER)) {
            return true;
        }

        // TODO: 허브 관리자는 delivery의 fromHubId/toHubId 중 하나를 관리하는지 확인 (허브 FeignClient 필요)
        if (role.equals(UserRole.HUB_MANAGER)) {
            // return hubService.isManagingHub(currUserId, delivery.getFromHubId())
            //         || hubService.isManagingHub(currUserId, delivery.getToHubId());
            return true; // 임시 허용
        }
        return false;
    }

    /**
     * 배송 조회 권한: 모든 로그인 사용자 (단, 허브 관리자와 배송 담당자는 자신이 담당하는 허브/배송만)
     */
    private boolean hasReadPermission(Delivery delivery, Long currUserid, UserRole role) {
//        if (role == UserRole.MASTER_ADMIN || role == UserRole.HUB_ADMIN || role == UserRole.PARTNER_AGENT) {
//            return true;
//        }

        if (role.equals(UserRole.MASTER) || role.equals(UserRole.SUPPLIER_MANAGER)) {
            return true;
        }

        // TODO: 허브 관리자는 delivery의 fromHubId/toHubId 중 하나를 관리하는지 확인 (허브 FeignClient 필요)
        if (role.equals(UserRole.HUB_MANAGER)) {
            // return hubService.isManagingHub(currUserId, delivery.getFromHubId())
            //         || hubService.isManagingHub(currUserId, delivery.getToHubId());
            return true; // 임시 허용
        }

        if (role == UserRole.DELIVERY_MANAGER) {
            return delivery.getDeliveryManagerId().equals(currUserid);
        }
        return true;
    }

    /**
     * 권한에 따른 검색 조건 보정
     */
    private DeliverySearchCondition refineSearchCondition(
        DeliverySearchCondition originalCondition,
        Long currUserId,
        UserRole role
    ) {
        if (role.equals(UserRole.MASTER) || role.equals(UserRole.SUPPLIER_MANAGER)) {
            return originalCondition;
        }

        // TODO: 허브 관리자는 delivery의 fromHubId/toHubId 중 하나를 관리하는지 확인 (허브 FeignClient 필요)
        if (role.equals(UserRole.HUB_MANAGER)) {
            // return hubService.isManagingHub(currUserId, delivery.getFromHubId())
            //         || hubService.isManagingHub(currUserId, delivery.getToHubId());
            return originalCondition; // 임시 허용
        }

        // 배송 담당자: 본인 담당 배송만 검색 가능하도록 조건 강제
        if (role.equals(UserRole.DELIVERY_MANAGER)) {
            if (originalCondition.deliveryManagerId() != null &&
                    !originalCondition.deliveryManagerId().equals(currUserId)) {
                throw new RuntimeException("배송 담당자는 본인의 배송 목록만 검색할 수 있습니다.");
            }

            return DeliverySearchCondition.builder()
                .hubId(originalCondition.hubId())
                .companyId(originalCondition.companyId())
                .deliveryManagerId(currUserId)
                .status(originalCondition.status())
                .build();
        }
        return originalCondition;
    }
}
