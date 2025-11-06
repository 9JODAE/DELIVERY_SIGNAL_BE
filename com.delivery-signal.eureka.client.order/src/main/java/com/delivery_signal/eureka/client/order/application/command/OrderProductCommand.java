package com.delivery_signal.eureka.client.order.application.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderProductCommand {
    private UUID productId;    // 상품 UUID
    private Integer quantity;        // 수량
    private String requestNote;  // 요청사항
}
