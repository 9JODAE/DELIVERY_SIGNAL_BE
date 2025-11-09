package com.delivery_signal.eureka.client.delivery.application.dto;

import lombok.Builder;
import org.springframework.data.domain.Sort;

@Builder
public record DeliveryListQuery(
    Integer page,
    Integer size,
    String sortBy,
    Sort.Direction direction
) {

    public static DeliveryListQuery of(Integer page, Integer size, String sortBy, Sort.Direction direction) {
        return new DeliveryListQuery(page, size, sortBy, direction);
    }
}
