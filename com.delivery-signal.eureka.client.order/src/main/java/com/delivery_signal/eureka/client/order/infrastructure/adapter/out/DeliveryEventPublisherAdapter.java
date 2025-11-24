package com.delivery_signal.eureka.client.order.infrastructure.adapter.out;

import com.delivery_signal.eureka.client.order.application.event.request.DeliveryCreateRequestEvent;
import com.delivery_signal.eureka.client.order.application.event.request.DeliveryDeleteRequest;
import com.delivery_signal.eureka.client.order.application.port.out.DeliveryEventPublisherPort;
import com.delivery_signal.eureka.client.order.infrastructure.config.RabbitConfig;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeliveryEventPublisherAdapter implements DeliveryEventPublisherPort {

    private final RabbitTemplate rabbitTemplate;

    /**
     * 배송 생성 요청 이벤트 발행
     */
    @Override
    public void publishDeliveryCreateRequestedEvent(DeliveryCreateRequestEvent event) {

        rabbitTemplate.convertAndSend(
            RabbitConfig.DELIVERY_EXCHANGE,
            RabbitConfig.ROUTING_KEY_DELIVERY_CREATE,
            event
        );

        log.info(
            "[ORDER→DELIVERY] 배송 생성 요청 이벤트 발행: orderId={}, supplier={}, receiver={}",
            event.orderId(),
            event.supplierCompanyId(),
            event.receiverCompanyId()
        );
    }

    /**
     * 배송 취소(삭제) 요청 이벤트 발행
     */
    @Override
    public void publishDeliveryCancelRequestedEvent(DeliveryDeleteRequest event) {

        rabbitTemplate.convertAndSend(
            RabbitConfig.DELIVERY_EXCHANGE,
            RabbitConfig.ROUTING_KEY_DELIVERY_CANCEL,
            event
        );

        log.info(
            "[ORDER→DELIVERY] 배송 취소 요청 이벤트 발행: deliveryId={}, requestedBy={}",
            event.deliveryId(),
            event.currUserId()
        );
    }
}
