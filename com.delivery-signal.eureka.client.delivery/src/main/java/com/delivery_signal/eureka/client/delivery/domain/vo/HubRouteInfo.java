package com.delivery_signal.eureka.client.delivery.domain.vo;

import java.util.UUID;

/**
 * Hub Service로부터 받는 경로 단계별 정보를 담는 Value Object
 */
public record HubRouteInfo(
    UUID departureHubId,
    UUID arrivalHubId,
    String departureHubName,
    String arrivalHubName,
    Double distance, // 예상 거리
    Long transitTime // 분 단위
) {}