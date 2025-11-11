package com.delivery_signal.eureka.client.delivery.presentation.dto.request;

public record RouteRecordUpdateRequest(
    Double actualDistance,
    Long actualTime,
    String newStatus
) {
}
