package com.delivery_signal.eureka.client.delivery.application.command;

import lombok.Builder;

@Builder
public record UpdateRouteRecordCommand(
    String newStatus,
    Double actualDistance,
    Long actualTime
) {
}
