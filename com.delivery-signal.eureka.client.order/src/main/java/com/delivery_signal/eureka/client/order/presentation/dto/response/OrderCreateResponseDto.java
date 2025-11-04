package com.delivery_signal.eureka.client.order.presentation.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public class OrderCreateResponseDto {

    UUID orderId;
    UUID createBy;
    LocalDateTime createAt;
    String message;

    public OrderCreateResponseDto(UUID orderId, UUID createBy, LocalDateTime createAt, String message) {
        this.orderId = orderId;
        this.createBy = createBy;
        this.createAt = createAt;
        this.message = message;
    }
}
