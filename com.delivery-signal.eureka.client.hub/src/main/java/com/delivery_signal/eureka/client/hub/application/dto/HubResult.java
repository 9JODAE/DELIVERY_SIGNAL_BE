package com.delivery_signal.eureka.client.hub.application.dto;

import java.util.UUID;

import com.delivery_signal.eureka.client.hub.domain.entity.Hub;

/**
 * 허브 조회 결과 DTO
 */
public record HubResult(
    UUID hubId,
    String name,
    String address,
    double latitude,
    double longitude
) {
    public static HubResult from(Hub hub) {
        return new HubResult(
            hub.getHubId(),
            hub.getName(),
            hub.getAddress().getValue(),
            hub.getCoordinate().getLatitude(),
            hub.getCoordinate().getLongitude()
        );
    }
}