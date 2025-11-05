package com.delivery_signal.eureka.client.order.infrastructure.repository;

import com.delivery_signal.eureka.client.order.domain.entity.Order;
import com.delivery_signal.eureka.client.order.domain.repository.OrderRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

import static com.delivery_signal.eureka.client.order.domain.entity.QOrder.order;
import static com.delivery_signal.eureka.client.order.domain.entity.QOrderProduct.orderProduct;

@Repository
public class OrderRepositoryImpl implements OrderRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public OrderRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Optional<Order> findByOrderId(UUID orderId) {
        return Optional.ofNullable(queryFactory
                .selectFrom(order)
                .leftJoin(order.orderProducts, orderProduct).fetchJoin()
                .where(order.id.eq(orderId))
                .fetchOne());
    }
}
