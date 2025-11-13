package com.delivery_signal.eureka.client.order.presentation.external.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class OrderCancelResponseDto {
    private UUID orderId;
    private UUID deliveryId;
    private String orderStatus;
    private String message;
}