package com.delivery_signal.eureka.client.order.domain.vo.hub;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class HubStockInfo {
    private final UUID productId;
    private final Integer stockQuantity;
}
