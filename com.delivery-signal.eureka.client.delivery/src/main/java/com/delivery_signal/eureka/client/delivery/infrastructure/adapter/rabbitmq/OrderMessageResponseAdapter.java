package com.delivery_signal.eureka.client.delivery.infrastructure.adapter.rabbitmq;

import com.delivery_signal.eureka.client.delivery.application.dto.event.DeliveryCreatedRequestEvent;
import com.delivery_signal.eureka.client.delivery.application.port.out.OrderMessageResponsePort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * [Outbound Adapter]
 * OrderMessageResponsePort를 구현하여 RabbitMQ로 배송 생성 완료 응답 메시지를 발행
 */
@Slf4j
@Component
public class OrderMessageResponseAdapter implements OrderMessageResponsePort {

    private final RabbitTemplate rabbitTemplate;

    @Value("${spring.rabbitmq.exchange.name}")
    private String exchangeName;

    @Value("${spring.rabbitmq.response.routing.key}")
    private String responseRoutingKey;

    public OrderMessageResponseAdapter(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void sendDeliveryCreatedResponse(DeliveryCreatedRequestEvent event) {
        log.info(
            "[DELIVERY -> ORDER] 배송 생성 완료 응답 메시지 발행: Exchange={}, Key={}, deliveryId={}, orderId={}",
            exchangeName,
            responseRoutingKey,
            event.deliveryId(),
            event.orderId()
        );
        // DTO 객체(event)를 RabbitTemplate이 MessageConverter(JSON)를 사용해 변환 후 발행
        rabbitTemplate.convertAndSend(exchangeName, responseRoutingKey, event);
    }
}
