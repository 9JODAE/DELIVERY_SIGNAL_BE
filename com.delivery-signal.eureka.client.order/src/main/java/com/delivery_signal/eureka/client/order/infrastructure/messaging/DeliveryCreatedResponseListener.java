package com.delivery_signal.eureka.client.order.infrastructure.messaging;

import com.delivery_signal.eureka.client.order.application.event.response.DeliveryCreatedResponseEvent;
import com.delivery_signal.eureka.client.order.application.port.out.OrderCommandPort;
import com.delivery_signal.eureka.client.order.application.port.out.OrderQueryPort;
import com.delivery_signal.eureka.client.order.common.NotFoundException;
import com.delivery_signal.eureka.client.order.domain.entity.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeliveryCreatedResponseListener {

    private final OrderQueryPort orderQueryPort;
    private final OrderCommandPort orderCommandPort;

    @RabbitListener(queues = "delivery.created.response.queue")
    public void onDeliveryCreatedResponse(DeliveryCreatedResponseEvent event) {

        log.info("[DELIVERY→ORDER] 배송 생성 완료 이벤트 수신: orderId={}, deliveryId={}",
            event.orderId(), event.deliveryId());

        // ✔ QueryPort: Optional 반환
        Order order = orderQueryPort.findByOrderId(event.orderId())
            .orElseThrow(() -> new NotFoundException("주문", event.orderId()));

        // ✔ 도메인 메서드로 상태변경
        order.assignDeliveryId(event.deliveryId());

        // ✔ CommandPort로 영속화
        orderCommandPort.save(order);

        log.info("[DELIVERY→ORDER] 주문에 배송 ID 반영 완료: orderId={}, deliveryId={}",
            event.orderId(), event.deliveryId());
    }
}
