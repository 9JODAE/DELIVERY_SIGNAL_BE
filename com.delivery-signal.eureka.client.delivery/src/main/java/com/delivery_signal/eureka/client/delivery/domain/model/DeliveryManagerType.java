package com.delivery_signal.eureka.client.delivery.domain.model;

import java.util.Arrays;

public enum DeliveryManagerType {
    HUB_DELIVERY("허브배송담당자"),
    PARTNER_DELIVERY("업체배송담당자");

    private final String description;

    private DeliveryManagerType(String description) {
        this.description = description;
    }

    public static DeliveryManagerType of(String status) {
        return Arrays.stream(DeliveryManagerType.values())
            .filter(s -> s.name().equalsIgnoreCase(status))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("ERROR: DeliveryManagerType not found."));
    }
}
