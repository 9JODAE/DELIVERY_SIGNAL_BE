package com.delivery_signal.eureka.client.order.infrastructure.client.delivery.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class DeliveryCreateRequestDto {
    private final UUID orderId;
    private final UUID companyId;
    private final String status;
    private final UUID departureHubId; //출발허브id
    private final UUID destinationHubId; //도착허브id
    private final String address;
    private final String recipient;
    private final String recipientSlackId;
}

