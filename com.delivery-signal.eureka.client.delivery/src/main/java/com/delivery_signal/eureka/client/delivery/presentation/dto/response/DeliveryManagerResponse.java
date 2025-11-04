package com.delivery_signal.eureka.client.delivery.presentation.dto.response;

import com.delivery_signal.eureka.client.delivery.domain.model.DeliveryManager;
import com.delivery_signal.eureka.client.delivery.domain.model.DeliveryManagerType;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;

@Builder
public record DeliveryManagerResponse(
    Long managerId,
    UUID hubId,
    String slackId,
    DeliveryManagerType managerType,
    Integer deliverySequence,
    LocalDateTime createdAt
) {

    public static DeliveryManagerResponse from(DeliveryManager manager) {
        return DeliveryManagerResponse.builder()
            .managerId(manager.getManagerId())
            .hubId(manager.getHubId())
            .slackId(manager.getSlackId())
            .managerType(manager.getManagerType())
            .deliverySequence(manager.getDeliverySequence())
            .createdAt(manager.getCreatedAt())
            .build();
    }
}
