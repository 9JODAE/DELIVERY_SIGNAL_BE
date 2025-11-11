package com.delivery_signal.eureka.client.order.application.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class OrderCreateResult {
    private UUID orderId;
    private Long createBy;
    private LocalDateTime createAt;
    private String message;
}
