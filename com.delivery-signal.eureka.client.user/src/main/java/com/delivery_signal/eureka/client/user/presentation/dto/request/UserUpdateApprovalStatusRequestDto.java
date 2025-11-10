package com.delivery_signal.eureka.client.user.presentation.dto.request;

import com.delivery_signal.eureka.client.user.domain.model.ApprovalStatus;

public record UserUpdateApprovalStatusRequestDto(
    ApprovalStatus approvalStatus
) { }
