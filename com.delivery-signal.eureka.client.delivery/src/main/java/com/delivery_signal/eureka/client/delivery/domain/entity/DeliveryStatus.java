package com.delivery_signal.eureka.client.delivery.domain.entity;

import lombok.Getter;

@Getter
public enum DeliveryStatus {
    // (경로 기록 초기 상태 / 배송 초기 상태)
    HUB_WAITING("허브 대기중"),
    HUB_MOVING("허브 이동중"),
    HUB_ARRIVED("목적지 허브 도착"), // (경로 기록 완료 상태)

    // Delivery 엔티티에만 사용 (최종 허브 -> 업체 이동)
    DELIVERING("업체 이동중"), // 업체 이동중
    DELIVERY_COMPLETED("배송 완료"), // 최종 배송 완료
    ISSUE_REPORTED("이슈/문제 발생");

    private final String description;

    DeliveryStatus(String description) {
        this.description = description;
    }
}
