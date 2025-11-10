package com.delivery_signal.eureka.client.order.application.validator;

import com.delivery_signal.eureka.client.order.common.ForbiddenException;
import com.delivery_signal.eureka.client.order.domain.vo.user.UserRole;
import org.springframework.stereotype.Component;

@Component
public class OrderPermissionValidator {

    // 생성: 로그인만 확인
    public void validateCreate(Long userId) {
        if (userId == null) throw new ForbiddenException("로그인이 필요합니다.");
    }

    // 수정
    public void validateUpdate(UserRole role, Long userId, Long orderHubId, Long userHubId) {
        switch (role) {
            case MASTER_ADMIN -> {}
            case HUB_ADMIN -> checkHubAdminPermission(orderHubId, userHubId, "수정");
            default -> throw new ForbiddenException("주문 수정 권한이 없습니다.");
        }
    }

    // 삭제
    public void validateDelete(UserRole role, Long userId, Long orderHubId, Long userHubId) {
        switch (role) {
            case MASTER_ADMIN -> {}
            case HUB_ADMIN -> checkHubAdminPermission(orderHubId, userHubId, "삭제");
            default -> throw new ForbiddenException("주문 삭제 권한이 없습니다.");
        }
    }

    // 조회
    public void validateRead(UserRole role, Long userId,
                             Long orderHubId, Long orderDeliveryManagerId, Long orderCompanyManagerId,
                             Long userHubId) {
        switch (role) {
            case MASTER_ADMIN -> {}
            case HUB_ADMIN -> checkHubAdminPermission(orderHubId, userHubId, "조회");
            case DELIVERY_MANAGER -> checkUserPermission(orderDeliveryManagerId, userId, "본인 담당 주문만 조회 가능합니다.");
            case COMPANY_MANAGER -> checkUserPermission(orderCompanyManagerId, userId, "본인 주문만 조회 가능합니다.");
            default -> throw new ForbiddenException("주문 조회 권한이 없습니다.");
        }
    }

    // ===================== 공통 메서드 =====================
    private void checkHubAdminPermission(Long orderHubId, Long userHubId, String action) {
        if (orderHubId == null || userHubId == null || !orderHubId.equals(userHubId)) {
            throw new ForbiddenException("해당 허브의 주문만 " + action + " 가능합니다.");
        }
    }

    private void checkUserPermission(Long orderUserId, Long currentUserId, String message) {
        if (orderUserId == null || currentUserId == null || !orderUserId.equals(currentUserId)) {
            throw new ForbiddenException(message);
        }
    }
}
