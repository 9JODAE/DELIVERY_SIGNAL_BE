package com.delivery_signal.eureka.client.order.domain.exception;

import com.delivery_signal.eureka.client.order.common.NotFoundException;

import java.util.UUID;

public class OrderNotFoundException extends NotFoundException {
    public OrderNotFoundException(UUID id) {
        super("주문이 존재하지 않습니다.", id);
    }
}
