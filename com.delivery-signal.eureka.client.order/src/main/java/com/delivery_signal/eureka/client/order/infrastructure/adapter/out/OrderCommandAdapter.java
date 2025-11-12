package com.delivery_signal.eureka.client.order.infrastructure.adapter.out;

import com.delivery_signal.eureka.client.order.application.port.out.OrderCommandPort;
import com.delivery_signal.eureka.client.order.domain.entity.Order;
import com.delivery_signal.eureka.client.order.domain.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderCommandAdapter implements OrderCommandPort {

    private final OrderRepository orderRepository;

    @Override
    public Order save(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public void delete(Order order) {
        orderRepository.delete(order);
    }
}
