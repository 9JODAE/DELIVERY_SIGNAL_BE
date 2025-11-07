package com.delivery_signal.eureka.client.order.presentation.external.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class UpdateOrderRequestDto {
    private UUID productId;
    private Integer transferQuantity;
    private String requestNote;
}
