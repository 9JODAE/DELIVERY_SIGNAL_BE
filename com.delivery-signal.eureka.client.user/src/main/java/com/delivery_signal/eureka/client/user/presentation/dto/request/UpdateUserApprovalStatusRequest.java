package com.delivery_signal.eureka.client.user.presentation.dto.request;

import com.delivery_signal.eureka.client.user.domain.entity.ApprovalStatus;

public record UpdateUserApprovalStatusRequest(
    ApprovalStatus approvalStatus
) { }
