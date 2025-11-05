package com.delivery_signal.eureka.client.delivery.presentation.dto.request;

import java.util.UUID;

// TODO: 루트 경로는 추후에 추가 예정
public record DeliveryCreateRequest(
    UUID orderId,
    String status,
    UUID departureHubId,
    UUID destinationHubId,
//    List<RouteSegmentDto> routes,
    String address,
    String recipient,
    String recipientSlackId,
    Long deliveryManagerId // 할당된 업체 배송 담당자 ID
) {
    public record RouteSegmentDto(
        Integer sequence, Long departureHubId, Long arrivalHubId,
        Double expectedDistance, Long expectedDuration) {}
}

