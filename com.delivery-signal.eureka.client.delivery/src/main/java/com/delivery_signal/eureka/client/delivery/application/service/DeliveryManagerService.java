package com.delivery_signal.eureka.client.delivery.application.service;

import com.delivery_signal.eureka.client.delivery.application.command.CreateDeliveryManagerCommand;
import com.delivery_signal.eureka.client.delivery.application.command.UpdateManagerCommand;
import com.delivery_signal.eureka.client.delivery.application.dto.ManagerQueryResponse;
import com.delivery_signal.eureka.client.delivery.application.validator.ManagerPermissionValidator;
import com.delivery_signal.eureka.client.delivery.domain.entity.DeliveryManager;
import com.delivery_signal.eureka.client.delivery.domain.entity.DeliveryManagerType;
import com.delivery_signal.eureka.client.delivery.domain.repository.DeliveryManagerRepository;
import com.delivery_signal.eureka.client.delivery.domain.service.DeliveryAssignmentService;
import java.util.NoSuchElementException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class DeliveryManagerService {

    private final DeliveryManagerRepository deliveryManagerRepository;
    private final DeliveryAssignmentService deliveryAssignmentService;
    private final ManagerPermissionValidator permissionValidator;

    public DeliveryManagerService(DeliveryManagerRepository deliveryManagerRepository,
        DeliveryAssignmentService deliveryAssignmentService, ManagerPermissionValidator permissionValidator) {
        this.deliveryManagerRepository = deliveryManagerRepository;
        this.deliveryAssignmentService = deliveryAssignmentService;
        this.permissionValidator = permissionValidator;
    }

    @Transactional
    public ManagerQueryResponse registerManager(Long userId, CreateDeliveryManagerCommand command) {
        permissionValidator.hasRegisterPermission(command.hubId(), userId);

        if (command.type() == DeliveryManagerType.PARTNER_DELIVERY && command.hubId() == null) {
            throw new IllegalStateException("업체 배송 담당자는 소속 허브 ID가 필수입니다.");
        }

        // 새로운 배송 담당자가 추가되면 가장 마지막 순번으로 설정
        Integer maxActiveSequence = deliveryManagerRepository.findMaxActiveSequence().orElse(-1);
        int newSequence = maxActiveSequence + 1;

        deliveryManagerRepository.findActiveById(command.managerId())
            .ifPresent(deliveryManager -> {
                throw new IllegalStateException("이미 등록되어 있는 배송 담당자입니다.");
            });

        DeliveryManager manager = DeliveryManager.create(command.managerId(),
            command.hubId(),
            command.slackId(),
            command.type(),
            newSequence,
            userId);

        DeliveryManager savedManager = deliveryManagerRepository.save(manager);
        return getManagerResponse(savedManager);
    }

    @Transactional(readOnly = true)
    public ManagerQueryResponse getDeliveryManagerInfo(Long managerId, Long currUserId) {
        DeliveryManager manager = getDeliveryManagerByManagerId(managerId);
        permissionValidator.hasReadPermission(manager, currUserId);
        return getManagerResponse(manager);
    }

    @Transactional
    public ManagerQueryResponse updateManager(Long managerId, UpdateManagerCommand command, Long currUserId, String role) {
        DeliveryManager manager = getDeliveryManagerByManagerId(managerId);
        permissionValidator.hasUpdatePermission(manager, currUserId);

        // 순번은 여기서 변경하지 않음. 순번 재배열은 별도 로직
        manager.update(command.hubId(), command.slackId(), command.type());
        return getManagerResponse(manager);
    }

    @Transactional
    public void softDeleteManager(Long managerId, Long deletedByUserId) {
        DeliveryManager manager = getDeliveryManagerByManagerId(managerId);
        permissionValidator.hasDeletePermission(manager, deletedByUserId);
        if (manager.isDeleted()) {
            throw new NoSuchElementException("이미 삭제된 배송 담당자입니다.");
        }
        // 논리적 삭제 처리
        manager.softDelete(deletedByUserId);
    }

    /**
     * 배송 담당자 순번을 관리하고 다음 담당자를 배정
     */
    @Transactional
    public Long assignNextDeliveryManager(Long currUserId) {
        // 활성 담당자 수 확인 (순환 로직 기반)
        Long activeCount = deliveryManagerRepository.countActiveManagers();
        if (activeCount == 0) {
            throw new IllegalStateException("현재 배정 가능한 배송 담당자가 없습니다.");
        }
        DeliveryManager nextManager = deliveryAssignmentService.getNextManagerForAssignment();
        permissionValidator.hasAssignPermission(nextManager.getHubId(), currUserId);
        // 배정된 담당자 ID 반환
        return nextManager.getManagerId();
    }

    private DeliveryManager getDeliveryManagerByManagerId(Long managerId) {
        return deliveryManagerRepository.findActiveById(managerId)
            .orElseThrow(() -> new NoSuchElementException("배송 담당자 정보를 찾을 수 없습니다"));
    }

    private ManagerQueryResponse getManagerResponse(DeliveryManager manager) {
        return ManagerQueryResponse.builder()
            .deliveryManagerId(manager.getManagerId())
            .hubId(manager.getHubId() != null ? manager.getHubId() : null)
            .slackId(manager.getSlackId())
            .managerType(manager.getManagerType())
            .deliverySequence(manager.getDeliverySequence())
            .createdAt(manager.getCreatedAt())
            .build();
    }
}
