package com.delivery_signal.eureka.client.order.infrastructure.adapter.out;

import com.delivery_signal.eureka.client.order.application.command.CreateDeliveryCommand;
import com.delivery_signal.eureka.client.order.application.port.out.DeliveryCommandPort;
import com.delivery_signal.eureka.client.order.domain.vo.delivery.DeliveryCreatedInfo;
import com.delivery_signal.eureka.client.order.infrastructure.client.delivery.DeliveryClient;
import com.delivery_signal.eureka.client.order.infrastructure.client.delivery.dto.DeliveryCreateRequestDto;
import com.delivery_signal.eureka.client.order.application.result.OrderForDeliveryResult;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

import static com.delivery_signal.eureka.client.order.domain.entity.QOrder.order;

@Slf4j
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

        return deliveryClient.createDelivery(infraRequest);
    }

    @Override
    public void cancelDelivery(UUID deliveryId) {
        try {
            // TODO: 현재 사용자 정보 주입 방식에 따라 수정 가능
            Long systemUserId = 0L; // 시스템 사용자 (ex: 주문 서비스 내부 호출)
            String systemRole = "MASTER"; // 기본 관리자 권한

            deliveryClient.cancelDelivery(deliveryId, systemUserId, systemRole);
            log.info("배송 취소 요청 완료: {}", deliveryId);

        } catch (FeignException.NotFound e) {
            log.warn("배송이 존재하지 않거나 이미 취소됨: {}", deliveryId);
        } catch (FeignException e) {
            log.error("배송 취소 요청 실패: {}", e.contentUTF8(), e);
            throw new RuntimeException("배송 취소 요청 실패", e);
        }
    }

}
