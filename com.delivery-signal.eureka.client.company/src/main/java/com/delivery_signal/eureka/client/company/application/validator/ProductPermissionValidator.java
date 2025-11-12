package com.delivery_signal.eureka.client.company.application.validator;

import com.delivery_signal.eureka.client.company.application.dto.UserAuthDto;
import com.delivery_signal.eureka.client.company.application.port.out.CompanyQueryPort;
import com.delivery_signal.eureka.client.company.application.port.out.HubQueryPort;
import com.delivery_signal.eureka.client.company.application.port.out.UserQueryPort;
import jakarta.ws.rs.ForbiddenException;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * 상품 권한 검증 Validator
 *
 * ┌────────────────────┬──────────────────────────────────────────────┐
 * │ 액션(Action)       │ 권한(Role)                                   │
 * ├────────────────────┼──────────────────────────────────────────────┤
 * │ 생성(Create)       │ MASTER_ADMIN, HUB_ADMIN(자기허브), COMPANY_MANAGER(본인업체) │
 * │ 수정(Update)       │ MASTER_ADMIN, HUB_ADMIN(자기허브), COMPANY_MANAGER(본인업체) │
 * │ 삭제(Delete)       │ MASTER_ADMIN, HUB_ADMIN(자기허브)             │
 * │ 조회(Read/Search)  │ MASTER_ADMIN, HUB_ADMIN(자기허브), DELIVERY_MANAGER, COMPANY_MANAGER │
 * └────────────────────┴──────────────────────────────────────────────┘
 */
@Component
public class ProductPermissionValidator {

    private final UserQueryPort userQueryPort;
    private final HubQueryPort hubQueryPort;
    private final CompanyQueryPort companyQueryPort;

    public ProductPermissionValidator(UserQueryPort userQueryPort, HubQueryPort hubQueryPort, CompanyQueryPort companyQueryPort) {
        this.userQueryPort = userQueryPort;
        this.hubQueryPort = hubQueryPort;
        this.companyQueryPort = companyQueryPort;
    }

    public void validateCreate(Long userId, UUID companyId, UUID hubId) {
        UserAuthDto user = getActiveUser(userId);
        checkHubExists(hubId);
        checkCompanyExists(companyId);

        switch (user.role()) {
            case "MASTER" -> {}
            case "HUB_MANAGER" -> checkHubPermission(user.organizationId(), hubId, "생성");
            case "SUPPLIER_MANAGER" -> checkCompanyPermission(user, companyId, "본인 업체의 상품만 생성 가능합니다.");
            default -> throwForbidden(user.role(), "상품 생성");
        }
    }

    public void validateUpdate(Long userId, UUID companyId, UUID hubId) {
        UserAuthDto user = getActiveUser(userId);

        switch (user.role()) {
            case "MASTER" -> {}
            case "HUB_MANAGER" -> checkHubPermission(user.organizationId(), hubId, "수정");
            case "SUPPLIER_MANAGER" -> checkCompanyPermission(user, companyId, "본인 업체의 상품만 수정 가능합니다.");
            default -> throwForbidden(user.role(), "상품 수정");
        }
    }

    public void validateDelete(Long userId, UUID hubId) {
        UserAuthDto user = getActiveUser(userId);

        switch (user.role()) {
            case "MASTER" -> {}
            case "HUB_ADMIN" -> checkHubPermission(user.organizationId(), hubId, "삭제");
            default -> throwForbidden(user.role(), "상품 삭제");
        }
    }

    public void validateRead(Long userId) {
        UserAuthDto user = getActiveUser(userId);
        if (user == null) throw new ForbiddenException("비활성 사용자");
    }

    // ================================================================
    // Private Utilities
    // ================================================================
    private void checkHubExists(UUID hubId) {
        if (!hubQueryPort.existsByHubId(hubId)) {
            throw new ForbiddenException("존재하지 않는 허브입니다.");
        }
    }

    private void checkCompanyExists(UUID companyId) {
        if (companyQueryPort.getCompanyById(companyId) == null) {
            throw new ForbiddenException("존재하지 않는 업체입니다.");
        }
    }

    private void checkHubPermission(UUID userHubId, UUID targetHubId, String action) {
        if (userHubId == null || !userHubId.equals(targetHubId)) {
            throw new ForbiddenException("해당 허브의 상품만 " + action + " 가능합니다.");
        }
    }

    private void checkCompanyPermission(UserAuthDto user, UUID companyId, String message) {
        if (!companyId.equals(user.organizationId())) {
            throw new ForbiddenException(message);
        }
    }

    private UserAuthDto getActiveUser(Long userId) {
        return userQueryPort.getUserAuthorizationInfo(String.valueOf(userId));
    }

    private void throwForbidden(String role, String action) {
        throw new ForbiddenException("권한이 없습니다. (role=" + role + ", action=" + action + ")");
    }
}
