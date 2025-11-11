package com.delivery_signal.eureka.client.order.domain.vo;

public enum OrderStatus {
    VALID,      // 정상 주문
    INVALID,    // 무효 (취소, 오류 등)
    CANCELED,   // 취소됨 (구분을 세분화하고 싶으면)
    DELIVERED;  // 배송 완료 등 나중에 확장 가능
}
