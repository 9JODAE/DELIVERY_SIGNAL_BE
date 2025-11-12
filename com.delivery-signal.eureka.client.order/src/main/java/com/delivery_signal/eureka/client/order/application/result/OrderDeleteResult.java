package com.delivery_signal.eureka.client.order.application.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDeleteResult {
    private UUID orderId;
    private Long deletedBy;
    private LocalDateTime deletedAt;
    private String message;
}
