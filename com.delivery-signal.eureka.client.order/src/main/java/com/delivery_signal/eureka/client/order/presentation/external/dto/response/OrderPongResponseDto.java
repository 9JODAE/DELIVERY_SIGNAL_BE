package com.delivery_signal.eureka.client.order.presentation.external.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
@AllArgsConstructor
public class OrderPongResponseDto {
    String toOrder;
    String status;
    Instant timestamp;
}
