package com.delivery_signal.eureka.client.user.domain.entity;

public enum ApprovalStatus {
    PENDING("PENDING"),    // 대기
    APPROVED("APPROVED"),   // 승인
    REJECTED("REJECTED");  // 거절

    private final String approveStatus;

    ApprovalStatus(String status) {
        approveStatus = status;  // ex) status: "OOO"
    }

    public String getApproveStatus() {
        return approveStatus;
    }
}
