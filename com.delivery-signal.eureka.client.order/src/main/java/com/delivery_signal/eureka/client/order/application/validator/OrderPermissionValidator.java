package com.delivery_signal.eureka.client.order.application.validator;

import com.delivery_signal.eureka.client.order.application.port.out.UserQueryPort;
import com.delivery_signal.eureka.client.order.common.ForbiddenException;
import com.delivery_signal.eureka.client.order.domain.vo.user.UserAuthorizationInfo;
import com.delivery_signal.eureka.client.order.domain.vo.user.UserRole;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * 주문 권한 검증 Validator (organizationId 기준)
 * <p>
 * 컨트롤러에서는 userId만 전달하며,
 * 내부에서 UserQueryPort를 통해 role과 organizationId를 조회 후 권한 검증 수행
 *
 * [권한 정책 요약]
 * ┌───────────────────────────────┬───────────────┐
 * │        액션(Action)          │     권한       │
 * ├──────────────────────────────┼───────────────┤
 * │ 생성(Create)                 │ SUPPLIER_MANAGER, MASTER │
 * │ 수정(Update)                 │ HUB_MANAGER(자기 허브), MASTER │
 * │ 취소(Cancel)                 │ SUPPLIER_MANAGER(본인 주문), HUB_MANAGER(자기 허브), MASTER │
 * │ 삭제(Delete - 논리삭제)       │ HUB_MANAGER(자기 허브), MASTER │
 * │ 조회(Read)                   │ SUPPLIER_MANAGER(본인 주문), HUB_MANAGER(자기 허브), MASTER │
 * │ 허브별 조회(ReadByHub)         │ HUB_MANAGER(자기 허브), MASTER │
 * └───────────────────────────────┴───────────────┘
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

    /** 주문 생성 권한 검증 - SUPPLIER_MANAGER, MASTER */
    public void validateCreate(Long userId) {
        UserAuthorizationInfo userInfo = getUserInfo(userId);
        UserRole role = userInfo.getRole();

        if (role != UserRole.SUPPLIER_MANAGER && role != UserRole.MASTER) {
            throw new ForbiddenException("주문 생성 권한이 없습니다. (업체 담당자 또는 마스터만 가능합니다)");
        }
    }

    /** 주문 수정 권한 검증 - MASTER 전체 / HUB_MANAGER 자기 허브만 가능 */
    public void validateUpdate(Long userId, UUID orderHubId) {
        UserAuthorizationInfo userInfo = getUserInfo(userId);
        UserRole role = userInfo.getRole();

        if (role == UserRole.MASTER) return;
        if (role == UserRole.HUB_MANAGER) {
            checkOrganizationPermission(orderHubId, userInfo.getOrganizationId(), "수정");
            return;
        }
        throwForbidden(role, "주문 수정");
    }

    /** 주문 취소 권한 검증 */
    public void validateCancel(Long userId, UUID orderHubId, Long orderCompanyManagerId) {
        UserAuthorizationInfo userInfo = getUserInfo(userId);
        UserRole role = userInfo.getRole();

        switch (role) {
            case MASTER -> {
                // 전체 취소 가능
            }
            case HUB_MANAGER -> checkOrganizationPermission(orderHubId, userInfo.getOrganizationId(), "취소");
            case SUPPLIER_MANAGER -> checkUserPermission(orderCompanyManagerId, userId, "본인 주문만 취소 가능합니다.");
            default -> throwForbidden(role, "주문 취소");
        }
    }

    /** 주문 삭제 권한 검증 - MASTER / HUB_MANAGER */
    public void validateDelete(Long userId, UUID orderHubId) {
        UserAuthorizationInfo userInfo = getUserInfo(userId);
        UserRole role = userInfo.getRole();

        if (role == UserRole.MASTER) return;
        if (role == UserRole.HUB_MANAGER) {
            checkOrganizationPermission(orderHubId, userInfo.getOrganizationId(), "삭제");
            return;
        }
        throwForbidden(role, "주문 삭제");
    }

    /** 주문 조회 권한 검증 */
    public void validateRead(Long userId, UUID orderHubId, Long orderCompanyManagerId) {
        UserAuthorizationInfo userInfo = getUserInfo(userId);
        UserRole role = userInfo.getRole();

        switch (role) {
            case MASTER -> {
                // 전체 조회 가능
            }
            case HUB_MANAGER -> checkOrganizationPermission(orderHubId, userInfo.getOrganizationId(), "조회");
            case SUPPLIER_MANAGER -> checkUserPermission(orderCompanyManagerId, userId, "본인 주문만 조회 가능합니다.");
            default -> throwForbidden(role, "주문 조회");
        }
    }

    /** 허브별 주문 조회 권한 검증 */
    public void validateReadByHub(Long userId, UUID orderHubId) {
        UserAuthorizationInfo userInfo = getUserInfo(userId);
        UserRole role = userInfo.getRole();

        if (role == UserRole.MASTER) return;
        if (role == UserRole.HUB_MANAGER) {
            checkOrganizationPermission(orderHubId, userInfo.getOrganizationId(), "허브별 조회");
            return;
        }
        throwForbidden(role, "허브별 주문 조회");
    }

    // ================================================================
    // PRIVATE UTILITIES
    // ================================================================

    /** null 체크만 하는 유저 조회 */
    private UserAuthorizationInfo getUserInfo(Long userId) {
        UserAuthorizationInfo info = userQueryPort.getUserAuthorizationInfo(userId);
        if (info == null) {
            throw new ForbiddenException("존재하지 않거나 비활성화된 사용자입니다.");
        }
        return info;
    }

    /** HUB_ADMIN / 조직 기반 검증 */
    private void checkOrganizationPermission(UUID resourceOrgId, UUID userOrgId, String action) {
        if (resourceOrgId == null || !resourceOrgId.equals(userOrgId)) {
            throw new ForbiddenException("해당 조직의 리소스만 " + action + " 가능합니다.");
        }
    }

    /** COMPANY_MANAGER / 주문 생성자 기반 검증 */
    private void checkUserPermission(Long orderUserId, Long currentUserId, String message) {
        if (orderUserId == null || !orderUserId.equals(currentUserId)) {
            throw new ForbiddenException(message);
        }
    }

    /** 공통 Forbidden 예외 발생 헬퍼 */
    private void throwForbidden(UserRole role, String action) {
        throw new ForbiddenException("권한이 없습니다. (role=" + role + ", action=" + action + ")");
    }
}
