package com.delivery_signal.eureka.client.order.infrastructure.adapter.out;

import com.delivery_signal.eureka.client.order.application.command.CreateDeliveryCommand;
import com.delivery_signal.eureka.client.order.application.port.out.DeliveryCommandPort;
import com.delivery_signal.eureka.client.order.domain.vo.delivery.DeliveryCreatedInfo;
import com.delivery_signal.eureka.client.order.infrastructure.client.delivery.DeliveryClient;
import com.delivery_signal.eureka.client.order.infrastructure.client.delivery.dto.DeliveryCreateRequestDto;
import com.delivery_signal.eureka.client.order.application.result.OrderForDeliveryResult;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

import static com.delivery_signal.eureka.client.order.domain.entity.QOrder.order;

@Component
public class DeliveryCommandAdapter implements DeliveryCommandPort {

    private final JPAQueryFactory queryFactory;
    private final DeliveryClient deliveryClient;

    public DeliveryCommandAdapter(JPAQueryFactory queryFactory, DeliveryClient deliveryClient) {
        this.queryFactory = queryFactory;
        this.deliveryClient = deliveryClient;
    }

    @Override
    public Optional<OrderForDeliveryResult> findOrderForDeliveryById(UUID orderId) {
        OrderForDeliveryResult dto = queryFactory
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

        return Optional.ofNullable(dto);
    }

    @Override
    public DeliveryCreatedInfo createDelivery(CreateDeliveryCommand appRequest) {
        DeliveryCreateRequestDto infraRequest = DeliveryCreateRequestDto.builder()
                .deliveryId(appRequest.getDeliveryId())
                .orderId(appRequest.getOrderId())
                .supplierCompanyId(appRequest.getSupplierCompanyId())
                .receiverCompanyId(appRequest.getReceiverCompanyId())
                .fromHubId(appRequest.getFromHubId())
                .toHubId(appRequest.getToHubId())
                .build();

        return deliveryClient.createDelivery(infraRequest); // 실제 외부 API 호출
    }
}
