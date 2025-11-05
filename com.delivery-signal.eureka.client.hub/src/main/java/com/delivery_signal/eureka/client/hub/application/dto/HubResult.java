package com.delivery_signal.eureka.client.hub.application.dto;

import java.util.UUID;

import com.delivery_signal.eureka.client.hub.domain.model.Hub;

public record HubResult(
    UUID hubId,
    String name,
    String address,
    Double latitude,
    Double longitude
) {
    public static HubResult from(Hub hub) {
        return new HubResult(
            hub.getHubId(),
            hub.getName(),
            hub.getAddress(),
            hub.getLatitude(),
            hub.getLongitude()
        );
    }
}