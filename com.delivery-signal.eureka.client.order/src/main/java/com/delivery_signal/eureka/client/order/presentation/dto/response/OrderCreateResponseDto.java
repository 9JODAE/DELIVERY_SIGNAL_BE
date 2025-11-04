package com.delivery_signal.eureka.client.order.presentation.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public class OrderCreateResponseDto {

    UUID orderId;
    UUID createBy;
    LocalDateTime createAt;

    public OrderCreateResponseDto(UUID orderId, UUID createBy, LocalDateTime createAt) {
        this.orderId = orderId;
        this.createBy = createBy;
        this.createAt = createAt;
    }
}
