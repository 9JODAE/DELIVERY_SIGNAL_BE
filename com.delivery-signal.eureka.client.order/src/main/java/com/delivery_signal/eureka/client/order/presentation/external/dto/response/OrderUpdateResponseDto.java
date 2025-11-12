package com.delivery_signal.eureka.client.order.presentation.external.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class OrderUpdateResponseDto {
    private UUID orderId;
    private Long updatedBy;
    private LocalDateTime updatedAt;
    private String message;
}
