package com.delivery_signal.eureka.client.order.presentation.external.dto;

import java.time.Instant;

// 응답 DTO
public record OrderPongResponseDto(
        String toOrder,
        String status,
        Instant timestamp
) {
}
