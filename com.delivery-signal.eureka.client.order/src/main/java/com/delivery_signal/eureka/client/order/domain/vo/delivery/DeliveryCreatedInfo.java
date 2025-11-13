package com.delivery_signal.eureka.client.order.domain.vo.delivery;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class DeliveryCreatedInfo {
    private final String message; // "배송 생성 완료"
    private final Long managerId;
    private final UUID hubId;
    private final String slackId;
    private final DeliveryManagerType managerType;
    private final Integer deliverySequence;
    private final LocalDateTime createdAt;
}