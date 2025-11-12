package com.delivery_signal.eureka.client.delivery.application.validator;

import com.delivery_signal.eureka.client.delivery.application.port.HubPort;
import com.delivery_signal.eureka.client.delivery.application.port.UserAuthPort;
import com.delivery_signal.eureka.client.delivery.common.UserRole;
import com.delivery_signal.eureka.client.delivery.domain.entity.Delivery;
import com.delivery_signal.eureka.client.delivery.domain.entity.DeliveryRouteRecords;
import com.delivery_signal.eureka.client.delivery.domain.vo.AuthorizedUser;
import com.delivery_signal.eureka.client.delivery.domain.vo.DeliverySearchCondition;
import com.delivery_signal.eureka.client.delivery.domain.vo.HubIdentifier;
import org.springframework.stereotype.Component;

@Component
public class DeliveryPermissionValidator {
    // Spring은 아래 Port들을 구현한 ServiceAdapter 인스턴스를 주입해줌
    private final UserAuthPort userAuthPort;
    private final HubPort hubPort;

    public DeliveryPermissionValidator(UserAuthPort userAuthPort, HubPort hubPort) {
        this.userAuthPort = userAuthPort;
        this.hubPort = hubPort;
    }

    /**
     * 배송 업데이트 권한: 마스터 관리자, 해당 허브 관리자, 해당 배송 담당자만 가능
     */
    public void hasUpdatePermission(Delivery delivery, Long updatorId) {
        // Port를 통해 인가 정보 획득
        AuthorizedUser authorizedUser = getAuthorizedUser(updatorId);
        UserRole role = UserRole.valueOf(authorizedUser.role());

        if (role.equals(UserRole.MASTER)) {
            return;
        }

        // 허브 관리자가 delivery의 출발지 허브 ID/목적지 허브 ID 중 하나를 관리하는지 확인
        if (role.equals(UserRole.HUB_MANAGER)) {
            // 출발 허브 유효성 검증
            if (!validateHubExistence(HubIdentifier.of(delivery.getDepartureHubId()), updatorId, authorizedUser.role())) {
                throw new IllegalArgumentException("출발 허브 ID가 유효하지 않습니다.");
            }

            // 목적지 허브 유효성 검증
            if (!validateHubExistence(HubIdentifier.of(delivery.getDestinationHubId()), updatorId, authorizedUser.role())) {
                throw new IllegalArgumentException("목적지 허브 ID가 유효하지 않습니다.");
            }
            return;
        }

        if (role.equals(UserRole.DELIVERY_MANAGER) && delivery.getDeliveryManagerId().equals(updatorId)) {
           return;
        }
        throw new RuntimeException("배송 상태를 수정할 권한이 없습니다. (ROLE: " + role + ")");
    }

    /**
     * 사용자 정보 조회
     */
    private AuthorizedUser getAuthorizedUser(Long userId) {
        AuthorizedUser user = userAuthPort.fetchUserAuthorizationInfo(userId);
        if (user == null) {
            throw new RuntimeException("사용자가 존재하지 않습니다.");
        }
        return user;
    }

    /**
     * 배송의 출발/목적지 허브 ID가 허브 관리자의 소속 허브 ID와 일치하는지 확인
     * (허브 관리자이면서 해당 허브의 소속인지 확인)
     */
    public boolean validateHubExistence(HubIdentifier hubId, Long currUserId, String role) {
        boolean exists = hubPort.existsById(hubId, currUserId, role);
        if (!exists) {
            throw new IllegalArgumentException("유효하지 않거나 활성화되지 않은 허브 ID입니다: " + hubId);
        } else {
            return true;
        }
    }

    /**
     * 배송 경로 기록 업데이트(이력 추가) 권한 : 마스터, 허브 관리자, 허브 배송 담당자만 가능
     */
    public void hasHubMovementPermission(DeliveryRouteRecords record, Long currUserId) {
        AuthorizedUser authorizedUser = getAuthorizedUser(currUserId);
        UserRole role = UserRole.valueOf(authorizedUser.role());

        if (role.equals(UserRole.MASTER)) {
            return;
        }

        // 허브 관리자가 delivery의 출발지 허브 ID/목적지 허브 ID 중 하나를 관리하는지 확인
        if (role.equals(UserRole.HUB_MANAGER)) {
            // 출발 허브 유효성 검증
            if (!validateHubExistence(HubIdentifier.of(record.getDepartureHubId()), currUserId, authorizedUser.role())) {
                throw new IllegalArgumentException("출발 허브 ID가 유효하지 않습니다.");
            }

            // 목적지 허브 유효성 검증
            if (!validateHubExistence(HubIdentifier.of(record.getDestinationHubId()), currUserId, authorizedUser.role())) {
                throw new IllegalArgumentException("목적지 허브 ID가 유효하지 않습니다.");
            }
            return;
        }

        if (role.equals(UserRole.DELIVERY_MANAGER) && record.getHubDeliveryManagerId() != null
                && record.getHubDeliveryManagerId().equals(currUserId)) {
            return;
        }

        throw new RuntimeException("해당 허브 이동 정보를 기록/수정할 권한이 없습니다.");
    }
    /**
     * 배송 삭제 권한: 마스터 관리자, 허브 관리자(담당 허브)
     */
    public void hasDeletePermission(Delivery delivery, Long deletorId) {
        AuthorizedUser authorizedUser = getAuthorizedUser(deletorId);
        UserRole role = UserRole.valueOf(authorizedUser.role());

        if (role.equals(UserRole.MASTER)) {
            return;
        }

        // 허브 관리자가 delivery의 출발지 허브 ID/목적지 허브 ID 중 하나를 관리하는지 확인
        if (role.equals(UserRole.HUB_MANAGER)) {
            // 출발 허브 유효성 검증
            if (!validateHubExistence(HubIdentifier.of(delivery.getDepartureHubId()), deletorId, authorizedUser.role())) {
                throw new IllegalArgumentException("출발 허브 ID가 유효하지 않습니다.");
            }

            // 목적지 허브 유효성 검증
            if (!validateHubExistence(HubIdentifier.of(delivery.getDestinationHubId()), deletorId, authorizedUser.role())) {
                throw new IllegalArgumentException("목적지 허브 ID가 유효하지 않습니다.");
            }
            return;
        }

        throw new RuntimeException("배송 정보를 삭제할 권한이 없습니다. (ROLE: " + role + ")");
    }

    /**
     * 배송 조회 권한: 모든 로그인 사용자 (단, 허브 관리자와 배송 담당자는 자신이 담당하는 허브/배송만)
     */
    public void hasReadPermission(Delivery delivery, Long currUserid) {
        AuthorizedUser authorizedUser = getAuthorizedUser(currUserid);
        UserRole role = UserRole.valueOf(authorizedUser.role());

        if (role.equals(UserRole.MASTER) || role.equals(UserRole.SUPPLIER_MANAGER)) {
            return;
        }

        // 허브 관리자가 delivery의 출발지 허브 ID/목적지 허브 ID 중 하나를 관리하는지 확인
        if (role.equals(UserRole.HUB_MANAGER)) {
            // 출발 허브 유효성 검증
            if (!validateHubExistence(HubIdentifier.of(delivery.getDepartureHubId()), currUserid, authorizedUser.role())) {
                throw new IllegalArgumentException("출발 허브 ID가 유효하지 않습니다.");
            }

            // 목적지 허브 유효성 검증
            if (!validateHubExistence(HubIdentifier.of(delivery.getDestinationHubId()), currUserid, authorizedUser.role())) {
                throw new IllegalArgumentException("목적지 허브 ID가 유효하지 않습니다.");
            }
            return;
        }

        if (role.equals(UserRole.DELIVERY_MANAGER) && delivery.getDeliveryManagerId().equals(currUserid)) {
            return;
        }
        throw new RuntimeException("해당 배송의 경로 이력을 조회할 권한이 없습니다.");
    }

    /**
     * 권한에 따른 검색 조건 보정
     */
    public DeliverySearchCondition refineSearchCondition(
        DeliverySearchCondition originalCondition,
        Long currUserId
    ) {
        AuthorizedUser authorizedUser = getAuthorizedUser(currUserId);
        UserRole role = UserRole.valueOf(authorizedUser.role());

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
