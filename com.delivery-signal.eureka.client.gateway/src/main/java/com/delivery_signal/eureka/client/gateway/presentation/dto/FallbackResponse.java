package com.delivery_signal.eureka.client.gateway.presentation.dto;

import lombok.Builder;

@Builder
public record FallbackResponse(
    Integer statusCode,
    String error,
    String message
) {
}
