package com.delivery_signal.eureka.client.order.application.validator;

import com.delivery_signal.eureka.client.order.application.port.out.UserQueryPort;
import com.delivery_signal.eureka.client.order.common.ForbiddenException;
import com.delivery_signal.eureka.client.order.domain.vo.user.UserAuthorizationInfo;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * 주문 권한 검증 Validator
 *
 * 컨트롤러에서는 userId만 전달하며,
 * 내부에서 UserQueryPort를 통해 role, active, hubId 조회 후 권한 검증을 수행합니다.
 *
 * [권한 정책 요약]
 * ┌───────────────────────────────┬───────────────┐
 * │        액션(Action)          │     권한       │
 * ├──────────────────────────────┼───────────────┤
 * │ 생성(Create)                 │ COMPANY_MANAGER, MASTER_ADMIN │
 * │ 수정(Update)                 │ HUB_ADMIN(자기 허브), MASTER_ADMIN │
 * │ 취소(Cancel)                 │ COMPANY_MANAGER(본인 주문), HUB_ADMIN(자기 허브), MASTER_ADMIN │
 * │ 삭제(Delete - 논리삭제)       │ HUB_ADMIN(자기 허브), MASTER_ADMIN │
 * │ 조회(Read)                   │ COMPANY_MANAGER(본인 주문), DELIVERY_MANAGER(본인 담당), HUB_ADMIN(자기 허브), MASTER_ADMIN │
 * │ 허브별 조회(ReadByHub)         │ HUB_ADMIN(자기 허브), MASTER_ADMIN │
 * └───────────────────────────────┴───────────────┘
 *
 * [비고]
 * - 취소(Cancel): 주문 상태 변경, 관련 데이터 상태 전파 (논리상태)
 * - 삭제(Delete): 운영자가 데이터 자체를 논리 삭제 (deleted_at / deleted_by)
 */
@Component
public class OrderPermissionValidator {

    private final UserQueryPort userQueryPort;

    public OrderPermissionValidator(UserQueryPort userQueryPort) {
        this.userQueryPort = userQueryPort;
    }

    // ================================================================
    // PUBLIC API — 권한 검증
    // ================================================================

    /** 주문 생성 권한 검증 - COMPANY_MANAGER, MASTER_ADMIN만 가능 */
    public void validateCreate(Long userId) {
        UserAuthorizationInfo userInfo = getActiveUser(userId);
        String role = userInfo.getRole();

        if (!role.equals("COMPANY_MANAGER") && !role.equals("MASTER_ADMIN")) {
            throw new ForbiddenException("주문 생성 권한이 없습니다. (업체 담당자 또는 마스터만 가능합니다)");
        }
    }

    /** 주문 수정 권한 검증 - MASTER_ADMIN 전체 / HUB_ADMIN 자기 허브만 가능 */
    public void validateUpdate(Long userId, UUID orderHubId) {
        UserAuthorizationInfo userInfo = getActiveUser(userId);
        String role = userInfo.getRole();

        switch (role) {
            case "MASTER_ADMIN" -> {}
            case "HUB_ADMIN" -> checkHubAdminPermission(orderHubId, userInfo.getHubId(), "수정");
            default -> throwForbidden(role, "주문 수정");
        }
    }

    /**
     * 주문 취소 권한 검증
     * - MASTER_ADMIN: 전체
     * - HUB_ADMIN: 자기 허브
     * - COMPANY_MANAGER: 본인 주문만 취소 가능
     */
    public void validateCancel(Long userId, UUID orderHubId, Long orderCompanyManagerId) {
        UserAuthorizationInfo userInfo = getActiveUser(userId);
        String role = userInfo.getRole();

        switch (role) {
            case "MASTER_ADMIN" -> {}
            case "HUB_ADMIN" -> checkHubAdminPermission(orderHubId, userInfo.getHubId(), "취소");
            case "COMPANY_MANAGER" -> checkUserPermission(orderCompanyManagerId, userId, "본인 주문만 취소 가능합니다.");
            default -> throwForbidden(role, "주문 취소");
        }
    }

    /**
     * 주문 삭제 권한 검증 (논리삭제용)
     * - MASTER_ADMIN: 전체
     * - HUB_ADMIN: 자기 허브
     * (일반 사용자/배송 담당자는 불가)
     */
    public void validateDelete(Long userId, UUID orderHubId) {
        UserAuthorizationInfo userInfo = getActiveUser(userId);
        String role = userInfo.getRole();

        switch (role) {
            case "MASTER_ADMIN" -> {}
            case "HUB_ADMIN" -> checkHubAdminPermission(orderHubId, userInfo.getHubId(), "삭제");
            default -> throwForbidden(role, "주문 삭제");
        }
    }

    /**
     * 주문 조회 권한 검증
     * - MASTER_ADMIN: 전체
     * - HUB_ADMIN: 자기 허브
     * - DELIVERY_MANAGER: 본인 담당 주문만 가능
     * - COMPANY_MANAGER: 본인 주문만 가능
     */
    public void validateRead(Long userId, UUID orderHubId, Long orderDeliveryManagerId, Long orderCompanyManagerId) {
        UserAuthorizationInfo userInfo = getActiveUser(userId);
        String role = userInfo.getRole();

        switch (role) {
            case "MASTER_ADMIN" -> {}
            case "HUB_ADMIN" -> checkHubAdminPermission(orderHubId, userInfo.getHubId(), "조회");
            case "DELIVERY_MANAGER" ->
                    checkUserPermission(orderDeliveryManagerId, userId, "본인 담당 주문만 조회 가능합니다.");
            case "COMPANY_MANAGER" ->
                    checkUserPermission(orderCompanyManagerId, userId, "본인 주문만 조회 가능합니다.");
            default -> throwForbidden(role, "주문 조회");
        }
    }

    /** 허브별 주문 조회 검증 - MASTER_ADMIN 전체 / HUB_ADMIN 자기 허브 */
    public void validateReadByHub(Long userId, UUID orderHubId) {
        UserAuthorizationInfo userInfo = getActiveUser(userId);
        String role = userInfo.getRole();

        switch (role) {
            case "MASTER_ADMIN" -> {}
            case "HUB_ADMIN" -> checkHubAdminPermission(orderHubId, userInfo.getHubId(), "허브별 조회");
            default -> throwForbidden(role, "허브별 주문 조회");
        }
    }

    // ================================================================
    // PRIVATE UTILITIES — 공통 검증 메서드
    // ================================================================

    /** 활성화된 사용자 정보 조회 */
    private UserAuthorizationInfo getActiveUser(Long userId) {
        UserAuthorizationInfo info = userQueryPort.getUserAuthorizationInfo(userId);
        if (info == null || !info.isActive()) {
            throw new ForbiddenException("비활성화된 사용자이거나 존재하지 않습니다.");
        }
        return info;
    }

    /** HUB_ADMIN 권한 검증 - 주문 허브와 사용자 허브 일치 여부 */
    private void checkHubAdminPermission(UUID orderHubId, UUID userHubId, String action) {
        if (orderHubId == null || !orderHubId.equals(userHubId)) {
            throw new ForbiddenException("해당 허브의 주문만 " + action + " 가능합니다.");
        }
    }

    /** 사용자별 권한 검증 - 주문 담당자/생성자와 로그인 사용자 일치 여부 */
    private void checkUserPermission(Long orderUserId, Long currentUserId, String message) {
        if (orderUserId == null || !orderUserId.equals(currentUserId)) {
            throw new ForbiddenException(message);
        }
    }

    /** 공통 Forbidden 예외 발생 헬퍼 */
    private void throwForbidden(String role, String action) {
        throw new ForbiddenException("권한이 없습니다. (role=" + role + ", action=" + action + ")");
    }
}
