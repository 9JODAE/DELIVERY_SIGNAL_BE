package com.delivery_signal.eureka.client.order.common;

import java.time.LocalDateTime;

public record ApiErrorResponse(String message, LocalDateTime timestamp) {
    public static ApiErrorResponse of(String message, LocalDateTime now) {
        return new ApiErrorResponse(message, LocalDateTime.now());
    }
}