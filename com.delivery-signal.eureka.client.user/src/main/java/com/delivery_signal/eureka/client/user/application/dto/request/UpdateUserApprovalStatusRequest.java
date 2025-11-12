package com.delivery_signal.eureka.client.user.application.dto.request;

import com.delivery_signal.eureka.client.user.domain.entity.ApprovalStatus;

public record UpdateUserApprovalStatusRequest(
    ApprovalStatus approvalStatus
) { }
