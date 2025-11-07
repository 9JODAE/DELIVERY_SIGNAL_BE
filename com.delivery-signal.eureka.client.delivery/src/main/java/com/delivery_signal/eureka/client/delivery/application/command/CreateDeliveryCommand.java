package com.delivery_signal.eureka.client.delivery.application.command;

import java.util.List;
import java.util.UUID;
import lombok.Builder;

@Builder
public record CreateDeliveryCommand(
    UUID orderId,
    UUID companyId,
    String status,
    UUID departureHubId,
    UUID destinationHubId,
    List<RouteSegmentCommand> routes,
    String address,
    String recipient,
    String recipientSlackId,
    Long deliveryManagerId // 할당된 업체 배송 담당자 ID
) {
}
