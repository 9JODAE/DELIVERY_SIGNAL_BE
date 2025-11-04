package com.delivery_signal.eureka.client.order.application.command;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class OrderProductCommand {
    private UUID productId;    // 상품 UUID
    private int quantity;        // 수량
    private String requestNote;  // 요청사항
}
