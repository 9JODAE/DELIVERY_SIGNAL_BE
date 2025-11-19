package com.delivery_signal.eureka.client.order.infrastructure.client.hub.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class GetStockQuantitiesRequestDto {
    List<UUID> productIds;
}
