package com.delivery_signal.eureka.client.order.application.port.out;

import com.delivery_signal.eureka.client.order.domain.entity.Order;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderQueryPort {
    Optional<Order> findByOrderId(UUID orderId);
    List<Order> findAllWithOrderProducts();
}