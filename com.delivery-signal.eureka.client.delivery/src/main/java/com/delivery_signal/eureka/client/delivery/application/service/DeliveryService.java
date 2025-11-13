package com.delivery_signal.eureka.client.delivery.application.service;

import com.delivery_signal.eureka.client.delivery.application.command.CreateDeliveryCommand;
import com.delivery_signal.eureka.client.delivery.application.command.SearchDeliveryCommand;
import com.delivery_signal.eureka.client.delivery.application.command.UpdateDeliveryInfoCommand;
import com.delivery_signal.eureka.client.delivery.application.command.UpdateDeliveryStatusCommand;
import com.delivery_signal.eureka.client.delivery.application.command.UpdateRouteRecordCommand;
import com.delivery_signal.eureka.client.delivery.application.dto.DeliveryListQuery;
import com.delivery_signal.eureka.client.delivery.application.dto.DeliveryQueryResponse;
import com.delivery_signal.eureka.client.delivery.application.port.HubPort;
import com.delivery_signal.eureka.client.delivery.common.exception.PermissionDeniedException;
import com.delivery_signal.eureka.client.delivery.domain.entity.DeliveryManager;
import com.delivery_signal.eureka.client.delivery.domain.repository.DeliveryManagerRepository;
import com.delivery_signal.eureka.client.delivery.domain.service.DeliveryAssignmentService;
import com.delivery_signal.eureka.client.delivery.domain.vo.DeliverySearchCondition;
import com.delivery_signal.eureka.client.delivery.application.dto.RouteRecordQueryResponse;
import com.delivery_signal.eureka.client.delivery.application.mapper.DeliveryDomainMapper;
import com.delivery_signal.eureka.client.delivery.application.validator.DeliveryPermissionValidator;
import com.delivery_signal.eureka.client.delivery.domain.entity.DeliveryRouteRecords;
import com.delivery_signal.eureka.client.delivery.domain.entity.DeliveryStatus;
import com.delivery_signal.eureka.client.delivery.domain.repository.DeliveryQueryRepository;
import com.delivery_signal.eureka.client.delivery.domain.repository.DeliveryRouteRecordsRepository;
import com.delivery_signal.eureka.client.delivery.application.dto.PagedDeliveryResponse;
import com.delivery_signal.eureka.client.delivery.common.UserRole;
import com.delivery_signal.eureka.client.delivery.domain.entity.Delivery;
import com.delivery_signal.eureka.client.delivery.domain.repository.DeliveryRepository;
import com.delivery_signal.eureka.client.delivery.domain.vo.HubIdentifier;
import com.delivery_signal.eureka.client.delivery.domain.vo.HubRouteInfo;
import com.delivery_signal.eureka.client.delivery.domain.vo.SlackMessageDetails;
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
    private final DeliveryManagerRepository deliveryManagerRepository;
    private final DeliveryDomainMapper deliveryDomainMapper;
    private final DeliveryPermissionValidator permissionValidator;
    private final DeliveryAssignmentService deliveryAssignmentService;
    private final DeliveryNotificationService deliveryNotificationService;
    private final HubPort hubPort;

    public DeliveryService(DeliveryRepository deliveryRepository,
        DeliveryQueryRepository deliveryQueryRepository,
        DeliveryRouteRecordsRepository deliveryRouteRecordsRepository,
        DeliveryManagerRepository deliveryManagerRepository,
        DeliveryDomainMapper deliveryDomainMapper, DeliveryPermissionValidator permissionValidator,
        HubPort hubPort, DeliveryAssignmentService deliveryAssignmentService,
        DeliveryNotificationService deliveryNotificationService) {
        this.deliveryRepository = deliveryRepository;
        this.deliveryRouteRecordsRepository = deliveryRouteRecordsRepository;
        this.deliveryQueryRepository = deliveryQueryRepository;
        this.deliveryManagerRepository = deliveryManagerRepository;
        this.deliveryDomainMapper = deliveryDomainMapper;
        this.permissionValidator = permissionValidator;
        this.hubPort = hubPort;
        this.deliveryAssignmentService = deliveryAssignmentService;
        this.deliveryNotificationService = deliveryNotificationService;
    }

    /**
     * Order Service에서 Delivery Service의 API를 호출하여 내부적으로 자동 생성
     * 새로운 주문에 대한 배송 및 전체 경로 기록 생성
     */
    @Transactional
    public DeliveryQueryResponse createDelivery(CreateDeliveryCommand command, Long creatorId) {
        // 출발지 허브 ID 기준 허브 유효성 검사
        if (!permissionValidator.validateHubExistence(HubIdentifier.of(command.departureHubId()), creatorId, "MASTER")) {
            throw new IllegalStateException("유효하지 않거나 활성화되지 않은 허브 ID입니다: " + command.departureHubId());
        }

        // 배송 담당자 할당 로직 수정 필요
        DeliveryManager initialHubDeliveryManager = assignInitialHubDeliveryManager();
        Long initialHubDeliveryManagerId = initialHubDeliveryManager.getManagerId();

        // 배송(허브 이동) 경로 요청 생성 및 저장
        List<HubRouteInfo> hubRouteInfos = hubPort.searchRoutes(
            HubIdentifier.of(command.departureHubId()), HubIdentifier.of(command.destinationHubId()));

        if (hubRouteInfos.isEmpty()) {
            throw new IllegalStateException("출발지(" + command.departureHubId() + ")에서 목적지(" + command.destinationHubId() + ")까지의 배송 경로를 찾을 수 없습니다.");
        }

        Delivery delivery = Delivery.create(command.orderId(),
            command.companyId(),
            command.status(),
            command.departureHubId(),
            command.destinationHubId(),
            command.address(),
            command.recipient(),
            command.recipientSlackId(),
            initialHubDeliveryManagerId,
            creatorId
        );
        Delivery savedDelivery = deliveryRepository.save(delivery);

        List<DeliveryRouteRecords> routeRecords = hubRouteInfos.stream()
            .map(segment ->
                DeliveryRouteRecords.initialCreate(
                    delivery,
                    initialHubDeliveryManager.getDeliverySequence(),
                    segment.departureHubId(),
                    segment.departureHubName(),
                    segment.arrivalHubId(),
                    segment.arrivalHubName(),
                    segment.distance(),
                    segment.transitTime(),
                    initialHubDeliveryManagerId,
                    creatorId
                ))
            .toList();

        // (허브 이동 정보) 경로 기록 리스트 저장
        deliveryRouteRecordsRepository.saveAll(routeRecords);
        return deliveryDomainMapper.toResponse(savedDelivery);
    }

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
        DeliverySearchCondition searchCondition = permissionValidator.refineSearchCondition(condition, currUserId);
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
            throw new PermissionDeniedException("담당 배송 목록을 조회할 권한이 없습니다. (ROLE: " + role + ")");
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
        permissionValidator.hasUpdatePermission(delivery, updatorId);

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
        permissionValidator.hasUpdatePermission(delivery, updatorId);

        // 상태 유효성 검사 : 마지막 최종 목적지 허브까지 배송이 완료된 시점(DELIVERING)이 아닐 경우 예외 처리
        if (!newStatus.equals(DeliveryStatus.DELIVERY_COMPLETED)) {
            throw new IllegalStateException("허용되지 않은 상태 변경입니다: " + newStatus.name());
        }

        delivery.updateStatus(newStatus, updatorId);

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
        permissionValidator.hasHubMovementPermission(record, updatorId);

        DeliveryStatus newStatus = DeliveryStatus.valueOf(command.newStatus());

        // 허브 간 이동 경로 상태 기록 (HUB_WAITING, HUB_MOVING 또는 HUB_ARRIVED 상태로만 가능)
        record.update(newStatus, command.actualDistance(),
            command.actualTime(), updatorId);

        // 최초 허브 출발 시 슬랙 메시지 전송 요청 로직 (최초 배송 출발 시)
        // 최초 허브 출발 시 (첫번째 시퀀스, HUB_MOVING) : Delivery 상태를 HUB_MOVING으로 변경
        if (newStatus.equals(DeliveryStatus.HUB_MOVING)) {
            delivery.updateStatus(DeliveryStatus.HUB_MOVING, updatorId);

            // 경로 정보 통합: 경유지 목록 생성, 첫번째 시퀀스 찾기
            List<DeliveryRouteRecords> allRecords = deliveryRouteRecordsRepository.findAllByDeliveryIdOrderBySequence(
                delivery.getDeliveryId()
            );

            if (!allRecords.isEmpty()) {
                // 슬랙 메시지 상세 정보 VO 생성
                SlackMessageDetails messageDetails = deliveryNotificationService.createSlackMessageDetails(
                    delivery, allRecords);
                try {
                    // 슬랙 메시지 전송 요청
                    deliveryNotificationService.requestSendSlackMessage(messageDetails);
                } catch (Exception e) {
                    // 알림은 핵심 비즈니스 로직(배송 기록)이 아니므로, 실패해도 트랜잭션을 롤백시키지 않고 로깅만
                    System.err.println("WARN: Slack 메시지 전송 요청 실패. 로깅 필요. 메시지: " + e.getMessage());
                }
            }
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
            throw new NoSuchElementException("이미 삭제된 배송 정보입니다.");
        }

        permissionValidator.hasDeletePermission(delivery, currUserId);
        List<DeliveryRouteRecords> routeRecords = deliveryRouteRecordsRepository.findAllByDeliveryIdOrderBySequence(
            delivery.getDeliveryId());
        routeRecords.forEach(record -> record.softDelete(currUserId));
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
        permissionValidator.hasReadPermission(delivery, currUserId);

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
        // 조회 및 검색 권한: 모든 로그인 사용자 가능, 단 배송 담당자는 자신이 담당하는 배송만 조회 가능
        permissionValidator.hasReadPermission(delivery, currUserId);
        return deliveryDomainMapper.toResponse(delivery);
    }

    /**
     * Hub 배송 담당자 할당
     */
    private DeliveryManager assignInitialHubDeliveryManager() {
        // 활성 담당자 수 확인 (순환 로직 기반)
        Long activeCount = deliveryManagerRepository.countActiveManagers();
        if (activeCount == 0) {
            throw new IllegalStateException("현재 배정 가능한 배송 담당자가 없습니다.");
        }
        return deliveryAssignmentService.getNextManagerForAssignment();
    }

    private Delivery getDelivery(UUID deliveryId) {
        return deliveryRepository.findActiveById(deliveryId)
            .orElseThrow(() -> new NoSuchElementException("배송 정보를 찾을 수 없습니다."));
    }

    private DeliveryRouteRecords getDeliveryRouteRecords(UUID routeId) {
        return deliveryRouteRecordsRepository.findActiveById(routeId)
            .orElseThrow(() -> new NoSuchElementException("배송 경로 기록을 찾을 수 없습니다."));
    }
}
