package com.delivery_signal.eureka.client.order.infrastructure.external;


import com.delivery_signal.eureka.client.order.application.port.out.ExternalOrderQueryPort;
import static com.delivery_signal.eureka.client.order.domain.entity.QOrder.order;
import com.delivery_signal.eureka.client.order.presentation.external.dto.response.OrderForDeliveryResponseDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ExternalOrderQueryRepositoryImpl implements ExternalOrderQueryPort {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<OrderForDeliveryResponseDto> findOrderForDeliveryById(UUID orderId) {
        OrderForDeliveryResponseDto dto = queryFactory
                .select(Projections.constructor(
                        OrderForDeliveryResponseDto.class,
                        order.id,
                        order.supplierCompanyId,
                        order.receiverCompanyId,
                        order.deliveryId,
                        order.requestNote
                ))
                .from(order)
                .where(order.id.eq(orderId))
                .fetchOne();

        return Optional.ofNullable(dto);
    }
}
