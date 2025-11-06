package com.delivery_signal.eureka.client.delivery.domain.model;

import lombok.Getter;

@Getter
public enum DeliveryStatus {
    HUB_WAITING("허브 대기중"),
    HUB_MOVING("허브 이동중"),
    HUB_ARRIVED("목적지 허브 도착"),
    DELIVERING("업체 배송중"),
    DELIVERY_COMPLETED("배송 완료"),
    ISSUE_REPORTED("이슈/문제 발생");

    private final String description;

    DeliveryStatus(String description) {
        this.description = description;
    }
}
