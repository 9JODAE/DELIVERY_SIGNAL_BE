package com.delivery_signal.eureka.client.order.domain.service;

import com.delivery_signal.eureka.client.order.domain.entity.Order;
import com.delivery_signal.eureka.client.order.domain.entity.OrderProduct;
import com.delivery_signal.eureka.client.order.domain.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderDomainService {

    private final OrderRepository orderRepository;

    public Order createOrder(UUID supplier, UUID receiver, String requestNote, List<OrderProduct> orderProducts, UUID deliveryId) {
        BigDecimal totalPrice = orderProducts.stream()
                .map(p -> p.getProductPriceAtOrder().multiply(BigDecimal.valueOf(p.getTransferQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return Order.builder()
                .supplierCompanyId(supplier)
                .receiverCompanyId(receiver)
                .requestNote(requestNote)
                .totalPrice(totalPrice)
                .deliveryId(deliveryId)
                .orderProducts(orderProducts)
                .build();
    }

    // OrderDomainService
    public List<Order> getOrdersByHub(UUID hubId) {
        return orderRepository.findAllWithOrderProducts().stream()
                .filter(order -> hubId.equals(order.getDepartureHubId())) // 출발 허브 기준
                .filter(order -> order.getDeletedAt() == null)       // 논리 삭제 제외
                .toList();
    }


}
