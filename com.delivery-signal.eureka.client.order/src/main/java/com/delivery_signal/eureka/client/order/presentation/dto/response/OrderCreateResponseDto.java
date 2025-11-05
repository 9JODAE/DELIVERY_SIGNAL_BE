package com.delivery_signal.eureka.client.order.presentation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class OrderCreateResponseDto {
    UUID orderId;
    UUID createBy;
    LocalDateTime createAt;
    String message;
}
