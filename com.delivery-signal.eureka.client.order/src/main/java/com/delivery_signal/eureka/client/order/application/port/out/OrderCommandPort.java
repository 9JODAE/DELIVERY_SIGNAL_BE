package com.delivery_signal.eureka.client.order.application.port.out;

import com.delivery_signal.eureka.client.order.domain.entity.Order;

public interface OrderCommandPort {
    Order save(Order order);
    void delete(Order order);
}
