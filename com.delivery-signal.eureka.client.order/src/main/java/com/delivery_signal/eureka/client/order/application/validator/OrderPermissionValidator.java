package com.delivery_signal.eureka.client.order.application.validator;

import com.delivery_signal.eureka.client.order.common.ForbiddenException;
import org.springframework.stereotype.Component;

@Component
public class OrderPermissionValidator {

    public static final String ROLE_MASTER_ADMIN = "MASTER_ADMIN";
    public static final String ROLE_HUB_ADMIN = "HUB_ADMIN";
    public static final String ROLE_DELIVERY_MANAGER = "DELIVERY_MANAGER";
    public static final String ROLE_COMPANY_MANAGER = "COMPANY_MANAGER";

    // 생성: 로그인만 확인
    public void validateCreate(Long userId) {
        if (userId == null) throw new ForbiddenException("로그인이 필요합니다.");
    }

    // 수정
    public void validateUpdate(String role, Long userId, Long orderHubId, Long userHubId) {
        switch (role) {
            case ROLE_MASTER_ADMIN -> {}
            case ROLE_HUB_ADMIN -> {
                if (!orderHubId.equals(userHubId))
                    throw new ForbiddenException("해당 허브의 주문만 수정 가능합니다.");
            }
            default -> throw new ForbiddenException("주문 수정 권한이 없습니다.");
        }
    }

    // 삭제
    public void validateDelete(String role, Long userId, Long orderHubId, Long userHubId) {
        switch (role) {
            case ROLE_MASTER_ADMIN -> {}
            case ROLE_HUB_ADMIN -> {
                if (!orderHubId.equals(userHubId))
                    throw new ForbiddenException("해당 허브의 주문만 삭제 가능합니다.");
            }
            default -> throw new ForbiddenException("주문 삭제 권한이 없습니다.");
        }
    }

    // 조회
    public void validateRead(String role, Long userId, Long orderHubId, Long orderDeliveryManagerId,
                             Long orderCompanyManagerId, Long userHubId) {
        switch (role) {
            case ROLE_MASTER_ADMIN -> {}
            case ROLE_HUB_ADMIN -> {
                if (!orderHubId.equals(userHubId))
                    throw new ForbiddenException("해당 허브의 주문만 조회 가능합니다.");
            }
            case ROLE_DELIVERY_MANAGER -> {
                if (!orderDeliveryManagerId.equals(userId))
                    throw new ForbiddenException("본인 담당 주문만 조회 가능합니다.");
            }
            case ROLE_COMPANY_MANAGER -> {
                if (!orderCompanyManagerId.equals(userId))
                    throw new ForbiddenException("본인 주문만 조회 가능합니다.");
            }
            default -> throw new ForbiddenException("주문 조회 권한이 없습니다.");
        }
    }
}
