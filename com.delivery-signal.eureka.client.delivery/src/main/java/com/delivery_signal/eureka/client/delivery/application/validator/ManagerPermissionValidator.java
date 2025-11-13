package com.delivery_signal.eureka.client.delivery.application.validator;

import com.delivery_signal.eureka.client.delivery.application.port.HubPort;
import com.delivery_signal.eureka.client.delivery.application.port.UserAuthPort;
import com.delivery_signal.eureka.client.delivery.common.UserRole;
import com.delivery_signal.eureka.client.delivery.domain.entity.DeliveryManager;
import com.delivery_signal.eureka.client.delivery.domain.vo.AuthorizedUser;
import com.delivery_signal.eureka.client.delivery.domain.vo.HubIdentifier;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class ManagerPermissionValidator {
    private final UserAuthPort userAuthPort;
    private final HubPort hubPort;

    public ManagerPermissionValidator(UserAuthPort userAuthPort, HubPort hubPort) {
        this.userAuthPort = userAuthPort;
        this.hubPort = hubPort;
    }

    /**
     * 배송 담당자 생성 권한 : 마스터 관리자, 해당 허브 관리자
     */
    public void hasRegisterPermission(UUID hubId, Long updatorId) {
        // Port를 통해 인가 정보 획득
        AuthorizedUser authorizedUser = getAuthorizedUser(updatorId);
        UserRole role = UserRole.valueOf(authorizedUser.role());

        if (role.equals(UserRole.MASTER)) {
            return;
        }

        // 허브 관리자의 소속 허브 유효성 검증
        if (role.equals(UserRole.HUB_MANAGER)) {
            if (!validateHubExistence(HubIdentifier.of(hubId), updatorId, authorizedUser.role())) {
                throw new IllegalArgumentException("허브 ID가 유효하지 않습니다.");
            }
            return;
        }
        throw new RuntimeException("배송 담당자를 등록할 권한이 없습니다. (ROLE: " + role + ")");
    }

    /**
     * 배송 담당자 순번 배정 권한 : 마스터 관리자, 해당 허브 관리자
     */
    public void hasAssignPermission(UUID hubId, Long updatorId) {
        // Port를 통해 인가 정보 획득
        AuthorizedUser authorizedUser = getAuthorizedUser(updatorId);
        UserRole role = UserRole.valueOf(authorizedUser.role());

        if (role.equals(UserRole.MASTER)) {
            return;
        }

        // 허브 관리자의 소속 허브 유효성 검증
        if (role.equals(UserRole.HUB_MANAGER)) {
            if (!validateHubExistence(HubIdentifier.of(hubId), updatorId, authorizedUser.role())) {
                throw new IllegalArgumentException("허브 ID가 유효하지 않습니다.");
            }
            return;
        }
        throw new RuntimeException("배송 담당자 순번을 배정할 권한이 없습니다. (ROLE: " + role + ")");
    }

    /**
     * 배송 담당자 조회 권한: 마스터 관리자, 허브 담당자, 배송 담당자 (본인 담당만)
     */
    public void hasReadPermission(DeliveryManager manager, Long currUserid) {
        AuthorizedUser authorizedUser = getAuthorizedUser(currUserid);
        UserRole role = UserRole.valueOf(authorizedUser.role());

        if (role.equals(UserRole.MASTER)) {
            return;
        }

        // 허브 관리자의 소속 허브 유효성 검증
        if (role.equals(UserRole.HUB_MANAGER)) {
            if (!validateHubExistence(HubIdentifier.of(manager.getHubId()), currUserid, authorizedUser.role())) {
                throw new IllegalArgumentException("허브 ID가 유효하지 않습니다.");
            }
            return;
        }

        if (role.equals(UserRole.DELIVERY_MANAGER) && manager.getManagerId().equals(currUserid)) {
            return;
        }

        throw new RuntimeException("배송 담당자를 조회할 권한이 없습니다.");
    }

    /**
     * 배송 담당자 정보 업데이트 권한: 마스터 관리자, 해당 허브 관리자
     */
    public void hasUpdatePermission(DeliveryManager manager, Long updatorId) {
        // Port를 통해 인가 정보 획득
        AuthorizedUser authorizedUser = getAuthorizedUser(updatorId);
        UserRole role = UserRole.valueOf(authorizedUser.role());

        if (role.equals(UserRole.MASTER)) {
            return;
        }

        // 허브 관리자의 소속 허브 유효성 검증
        if (role.equals(UserRole.HUB_MANAGER)) {
            if (!validateHubExistence(HubIdentifier.of(manager.getHubId()), updatorId, authorizedUser.role())) {
                throw new IllegalArgumentException("허브 ID가 유효하지 않습니다.");
            }
            return;
        }

        throw new RuntimeException("배송 담당자 정보를 수정할 권한이 없습니다. (ROLE: " + role + ")");
    }

    /**
     * 배송 담당자 삭제 권한: 마스터 관리자, 허브 관리자(담당 허브)
     */
    public void hasDeletePermission(DeliveryManager manager, Long deletorId) {
        AuthorizedUser authorizedUser = getAuthorizedUser(deletorId);
        UserRole role = UserRole.valueOf(authorizedUser.role());

        if (role.equals(UserRole.MASTER)) {
            return;
        }

        // 허브 관리자의 소속 허브 유효성 검증
        if (role.equals(UserRole.HUB_MANAGER)) {
            if (!validateHubExistence(HubIdentifier.of(manager.getHubId()), deletorId, authorizedUser.role())) {
                throw new IllegalArgumentException("허브 ID가 유효하지 않습니다.");
            }
            return;
        }

        throw new RuntimeException("배송 담당자를 삭제할 권한이 없습니다. (ROLE: " + role + ")");
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
}
