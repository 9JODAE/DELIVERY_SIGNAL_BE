package com.delivery_signal.eureka.client.order.infrastructure.adapter.out;

import com.delivery_signal.eureka.client.order.application.port.out.DeliveryQueryPort;
import com.delivery_signal.eureka.client.order.application.result.OrderForDeliveryResult;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

import static com.delivery_signal.eureka.client.order.domain.entity.QOrder.order;

/**
 * 배송 관련 조회 어댑터
 */
@Component
@RequiredArgsConstructor
public class DeliveryQueryAdapter implements DeliveryQueryPort {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<OrderForDeliveryResult> findOrderForDeliveryById(UUID orderId) {
        OrderForDeliveryResult result = queryFactory
                .select(Projections.constructor(
                        OrderForDeliveryResult.class,
                        order.id,
                        order.supplierCompanyId,
                        order.receiverCompanyId,
                        order.deliveryId,
                        order.requestNote
                ))
                .from(order)
                .where(order.id.eq(orderId))
                .fetchOne();

        return Optional.ofNullable(result);
    }
}
