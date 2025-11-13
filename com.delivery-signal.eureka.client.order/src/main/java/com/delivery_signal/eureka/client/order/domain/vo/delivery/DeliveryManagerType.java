package com.delivery_signal.eureka.client.order.domain.vo.delivery;

import lombok.Getter;

@Getter
public enum DeliveryManagerType {
    HUB_DELIVERY("허브배송담당자"),
    PARTNER_DELIVERY("업체배송담당자");

    private final String description;

    DeliveryManagerType(String description) {
        this.description = description;
    }

}
