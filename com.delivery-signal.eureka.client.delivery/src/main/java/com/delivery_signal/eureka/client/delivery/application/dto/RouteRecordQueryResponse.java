package com.delivery_signal.eureka.client.delivery.application.dto;

import com.delivery_signal.eureka.client.delivery.domain.entity.DeliveryStatus;
import java.util.UUID;
import lombok.Builder;

@Builder
public record RouteRecordQueryResponse(
    UUID routeId,
    UUID deliveryId,
    Double actualDistance,
    Long actualTime,
    String status,
    Long hubDeliveryManagerId
) {
}
