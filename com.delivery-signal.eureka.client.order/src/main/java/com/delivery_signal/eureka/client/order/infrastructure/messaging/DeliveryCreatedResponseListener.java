package com.delivery_signal.eureka.client.order.infrastructure.messaging;

import com.delivery_signal.eureka.client.order.application.event.response.DeliveryCreatedResponseEvent;
import com.delivery_signal.eureka.client.order.application.port.out.OrderCommandPort;
import com.delivery_signal.eureka.client.order.application.port.out.OrderQueryPort;
import com.delivery_signal.eureka.client.order.common.NotFoundException;
import com.delivery_signal.eureka.client.order.domain.entity.Order;

import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeliveryCreatedResponseListener {

    private final OrderQueryPort orderQueryPort;
    private final OrderCommandPort orderCommandPort;

    @RabbitListener(
            queues = "delivery.created.response.queue",
            ackMode = "MANUAL"
    )
    public void onDeliveryCreatedResponse(
            DeliveryCreatedResponseEvent event,
            Channel channel,
            @Header(AmqpHeaders.DELIVERY_TAG) long tag
    ) {

        log.info("[DELIVERY→ORDER] 배송 생성 완료 이벤트 수신: orderId={}, deliveryId={}",
                event.orderId(), event.deliveryId());

        try {
            // 1) 주문 조회
            Order order = orderQueryPort.findByOrderId(event.orderId())
                    .orElseThrow(() -> new NotFoundException("주문", event.orderId()));

            // 2) 배송 ID 도메인 반영
            order.assignDeliveryId(event.deliveryId());

            // 3) 저장
            orderCommandPort.save(order);

            // 4) 성공 ACK
            channel.basicAck(tag, false);

            log.info("[DELIVERY RESPONSE] 처리 성공, ack 완료: orderId={}, deliveryId={}",
                    event.orderId(), event.deliveryId());

        } catch (Exception e) {

            log.error("[DELIVERY RESPONSE] 처리 실패 → nack & 재큐잉: event={}", event, e);

            try {
                channel.basicNack(tag, false, true);
            } catch (Exception nackEx) {
                log.error("[DELIVERY RESPONSE] nack 처리 중 예외 발생!", nackEx);
            }
        }
    }
}
