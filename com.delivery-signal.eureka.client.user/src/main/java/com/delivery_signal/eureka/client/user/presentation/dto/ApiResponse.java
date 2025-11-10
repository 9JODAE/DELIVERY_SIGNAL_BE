package com.delivery_signal.eureka.client.user.presentation.dto;

import org.springframework.http.HttpStatus;

public record ApiResponse<T>(
        String message,
        T data
) {
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(null, data);
    }

    public static <T> ApiResponse<T> message(String message) {
        return new ApiResponse<>(message, null);
    }
}
