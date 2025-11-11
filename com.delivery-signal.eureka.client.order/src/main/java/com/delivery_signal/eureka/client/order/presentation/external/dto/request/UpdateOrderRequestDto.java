package com.delivery_signal.eureka.client.order.presentation.external.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateOrderRequestDto {
    private UUID productId;
    private Integer transferQuantity;
    private String requestNote;
}
