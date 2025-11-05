package com.delivery_signal.eureka.client.delivery.application.dto;

import com.delivery_signal.eureka.client.delivery.domain.model.DeliveryManager;
import com.delivery_signal.eureka.client.delivery.domain.model.DeliveryManagerType;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;

@Builder
public record ManagerQueryResponse(
    Long deliveryManagerId,
    UUID hubId,
    String slackId,
    DeliveryManagerType managerType,
    Integer deliverySequence,
    LocalDateTime createdAt
) {
    public static ManagerQueryResponse from(DeliveryManager manager) {
        return ManagerQueryResponse.builder()
            .deliveryManagerId(manager.getManagerId())
            .hubId(manager.getHubId() != null ? manager.getHubId() : null)
            .slackId(manager.getSlackId())
            .managerType(manager.getManagerType())
            .deliverySequence(manager.getDeliverySequence())
            .createdAt(manager.getCreatedAt())
            .build();
    }
}
