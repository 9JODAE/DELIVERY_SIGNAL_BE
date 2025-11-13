package com.delivery_signal.eureka.client.delivery.application.dto;

import com.delivery_signal.eureka.client.delivery.domain.entity.DeliveryManagerType;
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
}
