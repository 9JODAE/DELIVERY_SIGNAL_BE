package com.delivery_signal.eureka.client.company.application.validator;

import com.delivery_signal.eureka.client.company.application.port.out.HubQueryPort;
import com.delivery_signal.eureka.client.company.application.port.out.UserQueryPort;
import com.delivery_signal.eureka.client.company.domain.vo.user.UserAuthorizationInfo;
import jakarta.ws.rs.ForbiddenException;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * 업체 권한 검증 Validator
 *
 * [권한 정책 요약]
 * ┌────────────────────┬────────────────────────────┐
 * │ 액션(Action)       │ 권한(Role)                 │
 * ├────────────────────┼────────────────────────────┤
 * │ 생성(Create)       │ MASTER_ADMIN, HUB_ADMIN(자기허브) │
 * │ 수정(Update)       │ MASTER_ADMIN, HUB_ADMIN(자기허브), COMPANY_MANAGER(본인업체) │
 * │ 삭제(Delete)       │ MASTER_ADMIN, HUB_ADMIN(자기허브) │
 * │ 조회(Read/Search)  │ 전체 조회 가능 (단, deleted_at 제외) │
 * └────────────────────┴────────────────────────────┘
 */
@Component
public class CompanyPermissionValidator {

    private final UserQueryPort userQueryPort;
    private final HubQueryPort hubQueryPort;

    public CompanyPermissionValidator(UserQueryPort userQueryPort, HubQueryPort hubQueryPort) {
        this.userQueryPort = userQueryPort;
        this.hubQueryPort = hubQueryPort;
    }

    /** 업체 생성 - 허브 존재 여부 검증 + 권한 검증 */
    public void validateCreate(Long userId, UUID targetHubId) {
        UserAuthorizationInfo user = getActiveUser(userId);
        checkHubExists(targetHubId);

        switch (user.getRole()) {
            case "MASTER_ADMIN" -> {}
            case "HUB_ADMIN" -> checkHubPermission(user.getHubId(), targetHubId, "생성");
            default -> throwForbidden(user.getRole(), "업체 생성");
        }
    }

    /** 업체 수정 - MASTER_ADMIN 전체 / HUB_ADMIN(자기 허브) / COMPANY_MANAGER(본인 업체) */
    public void validateUpdate(Long userId, UUID companyHubId, Long companyManagerId) {
        UserAuthorizationInfo user = getActiveUser(userId);

        switch (user.getRole()) {
            case "MASTER_ADMIN" -> {}
            case "HUB_ADMIN" -> checkHubPermission(user.getHubId(), companyHubId, "수정");
            case "COMPANY_MANAGER" -> checkUserPermission(userId, companyManagerId, "본인 업체만 수정 가능합니다.");
            default -> throwForbidden(user.getRole(), "업체 수정");
        }
    }

    /** 업체 삭제 - MASTER_ADMIN 전체 / HUB_ADMIN(자기 허브) */
    public void validateDelete(Long userId, UUID companyHubId) {
        UserAuthorizationInfo user = getActiveUser(userId);

        switch (user.getRole()) {
            case "MASTER_ADMIN" -> {}
            case "HUB_ADMIN" -> checkHubPermission(user.getHubId(), companyHubId, "삭제");
            default -> throwForbidden(user.getRole(), "업체 삭제");
        }
    }

    /** 업체 조회는 전체 가능하지만 deleted_at != null 제외 */
    public void validateRead(Long userId) {
        UserAuthorizationInfo user = getActiveUser(userId);
        if (user == null) throw new ForbiddenException("비활성 사용자");
    }

    // ================================================================
    // PRIVATE UTILITIES
    // ================================================================
    private void checkHubExists(UUID hubId) {
        if (!hubQueryPort.existsByHubId(hubId)) {
            throw new ForbiddenException("존재하지 않는 허브입니다.");
        }
    }

    private void checkHubPermission(UUID userHubId, UUID targetHubId, String action) {
        if (userHubId == null || !userHubId.equals(targetHubId)) {
            throw new ForbiddenException("해당 허브의 업체만 " + action + " 가능합니다.");
        }
    }

    private void checkUserPermission(Long userId, Long ownerId, String message) {
        if (!userId.equals(ownerId)) throw new ForbiddenException(message);
    }

    private UserAuthorizationInfo getActiveUser(Long userId) {
        UserAuthorizationInfo info = userQueryPort.getUserAuthorizationInfo(userId);
        if (info == null || !info.isActive())
            throw new ForbiddenException("비활성화된 사용자이거나 존재하지 않습니다.");
        return info;
    }

    private void throwForbidden(String role, String action) {
        throw new ForbiddenException("권한이 없습니다. (role=" + role + ", action=" + action + ")");
    }
}
