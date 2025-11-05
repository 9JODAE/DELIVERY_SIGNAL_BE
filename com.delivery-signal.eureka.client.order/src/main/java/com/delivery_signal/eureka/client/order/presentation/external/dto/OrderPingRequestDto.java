package com.delivery_signal.eureka.client.order.presentation.external.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class OrderPingRequestDto {
    String from;
}

