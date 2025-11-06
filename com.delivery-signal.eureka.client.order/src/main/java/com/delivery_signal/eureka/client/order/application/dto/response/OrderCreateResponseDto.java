package com.delivery_signal.eureka.client.order.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class OrderCreateResponseDto {
    private UUID orderId;
    private Long createBy;
    private LocalDateTime createAt;
    private String message;
}
