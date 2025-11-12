package com.delivery_signal.eureka.client.user.application.dto;

import com.delivery_signal.eureka.client.user.domain.entity.ApprovalStatus;

public enum ApprovalStatusType {
    PENDING,     // 승인 대기
    APPROVED,    // 승인 완료
    REJECTED;    // 승인 거절

    /**
     * Domain -> Application 변환
     */
    public static ApprovalStatusType from(ApprovalStatus status) {
        if (status == null) return null;
        return ApprovalStatusType.valueOf(status.name());
    }

    /**
     * Application -> Domain 변환
     */
    public ApprovalStatus toDomain() {
        return ApprovalStatus.valueOf(this.name());
    }
}
