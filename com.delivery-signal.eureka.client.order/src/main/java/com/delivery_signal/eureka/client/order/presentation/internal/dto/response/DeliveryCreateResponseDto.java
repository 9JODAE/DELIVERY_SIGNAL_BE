package com.delivery_signal.eureka.client.order.presentation.internal.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class DeliveryCreateResponseDto {

    private UUID deliveryId;        // 새로 생성된 배송 ID
    private UUID orderId;           // 해당 배송이 속한 주문 ID
    private String status;          // 배송 상태 (예: CREATED, IN_PROGRESS, COMPLETED)
    private Instant createdAt;      // 생성 시각
}