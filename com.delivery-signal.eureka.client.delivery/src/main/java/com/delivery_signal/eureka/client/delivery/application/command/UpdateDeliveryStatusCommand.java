package com.delivery_signal.eureka.client.delivery.application.command;

import java.util.UUID;
import lombok.Builder;

@Builder
public record UpdateDeliveryStatusCommand(
    String newStatus
) {
}
