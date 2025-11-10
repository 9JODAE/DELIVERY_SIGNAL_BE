package com.delivery_signal.eureka.client.delivery.application.command;

import java.util.UUID;
import lombok.Builder;

@Builder
public record RouteSegmentCommand(
    Integer sequence, // 배송 경로 상 허브의 순번
    UUID departureHubId, // 현재 구간의 출발 허브
    UUID destinationHubId,   // 현재 구간의 도착 허브
    Double estDistance,
    Long estTime
    // 허브 간 배송 담당자 ID는 후속 로직에서 결정
) {
}
