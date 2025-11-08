package com.delivery_signal.eureka.client.order.domain.vo.delivery;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DeliveryCreatedInfo {
    private final String message; // 예: "배송 생성 완료"
}
