package com.delivery_signal.eureka.client.delivery.application.dto;

import com.delivery_signal.eureka.client.delivery.domain.entity.DeliveryStatus;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;

@Builder
public record RouteRecordQueryResponse(
    UUID routeId,
    UUID deliveryId,
    Integer sequence,
    UUID departureHubId,
    UUID destinationHubId,
    Double actualDistance,
    Long actualTime,
    String status,
    Long hubDeliveryManagerId,
    LocalDateTime createdAt
) {
}
