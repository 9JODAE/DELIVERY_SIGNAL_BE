package com.delivery_signal.eureka.client.order.infrastructure.adapter;

import com.delivery_signal.eureka.client.order.application.port.out.OrderQueryPort;
import com.delivery_signal.eureka.client.order.domain.entity.Order;
import com.delivery_signal.eureka.client.order.domain.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OrderQueryAdapter implements OrderQueryPort {

    private final OrderRepository orderRepository;

    @Override
    public Optional<Order> findByOrderId(UUID orderId) {
        return orderRepository.findByOrderId(orderId);
    }


    @Override
    public List<Order> findAllWithOrderProducts() {
        return orderRepository.findAllWithOrderProducts();
    }
}
