package com.delivery_signal.eureka.client.delivery.application.validator;

import com.delivery_signal.eureka.client.delivery.application.port.UserAuthPort;
import com.delivery_signal.eureka.client.delivery.common.UserRole;
import com.delivery_signal.eureka.client.delivery.domain.entity.Delivery;
import com.delivery_signal.eureka.client.delivery.domain.entity.DeliveryRouteRecords;
import com.delivery_signal.eureka.client.delivery.domain.vo.AuthorizedUser;
import org.springframework.stereotype.Component;

@Component
public class DeliveryPermissionValidator {
    private final UserAuthPort userAuthPort;

    public DeliveryPermissionValidator(UserAuthPort userAuthPort) {
        this.userAuthPort = userAuthPort;
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

        // TODO: 허브 관리자는 delivery의 fromHubId/toHubId 중 하나를 관리하는지 확인 (허브 FeignClient 필요)
        // 허브 관리자이면서 해당 허브의 소속인지 확인
        if (role.equals(UserRole.HUB_MANAGER)) {
            // return hubService.isManagingHub(currUserId, delivery.getFromHubId())
            //         || hubService.isManagingHub(currUserId, delivery.getToHubId());
            return; // 임시 허용
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
     * 배송 경로 기록 업데이트(이력 추가) 권한 : 마스터, 허브 관리자, 허브 배송 담당자만 가능
     */
    public void hasHubMovementPermission(DeliveryRouteRecords record, Long currUserId) {
        AuthorizedUser authorizedUser = getAuthorizedUser(currUserId);
        UserRole role = UserRole.valueOf(authorizedUser.role());

        if (role.equals(UserRole.MASTER)) {
            return;
        }

        // TODO: 허브 FeignClient 호출을 통해 currUserId가 record.departureHubId 또는 record.destinationHubId를 담당하는지 확인
        if (role.equals(UserRole.HUB_MANAGER)) {
            // return hubService.isManagingHub(currUserId, record.getFromHubId())
            //         || hubService.isManagingHub(currUserId, record.getToHubId());
            return; // 임시 허용
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

        // TODO: 허브 관리자는 delivery의 fromHubId/toHubId 중 하나를 관리하는지 확인 (허브 FeignClient 필요)
        if (role.equals(UserRole.HUB_MANAGER)) {
            // return hubService.isManagingHub(currUserId, delivery.getFromHubId())
            //         || hubService.isManagingHub(currUserId, delivery.getToHubId());
            return; // 임시 허용
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

        // TODO: 허브 관리자는 delivery의 fromHubId/toHubId 중 하나를 관리하는지 확인 (허브 FeignClient 필요)
        if (role.equals(UserRole.HUB_MANAGER)) {
            // return hubService.isManagingHub(currUserId, delivery.getFromHubId())
            //         || hubService.isManagingHub(currUserId, delivery.getToHubId());
            return;
        }

        if (role.equals(UserRole.DELIVERY_MANAGER) && delivery.getDeliveryManagerId().equals(currUserid)) {
            return;
        }
        throw new RuntimeException("해당 배송의 경로 이력을 조회할 권한이 없습니다.");
    }
}
