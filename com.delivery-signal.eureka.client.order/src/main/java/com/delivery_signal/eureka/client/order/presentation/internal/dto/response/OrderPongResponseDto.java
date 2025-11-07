package com.delivery_signal.eureka.client.order.presentation.internal.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
@AllArgsConstructor
public class OrderPongResponseDto {
    private String toOrder;
    private String status;
    private Instant timestamp;
}
