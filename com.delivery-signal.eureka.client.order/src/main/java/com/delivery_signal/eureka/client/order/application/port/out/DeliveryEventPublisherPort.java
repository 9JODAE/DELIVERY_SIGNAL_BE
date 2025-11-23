package com.delivery_signal.eureka.client.order.application.port.out;

import com.delivery_signal.eureka.client.order.application.event.request.DeliveryCreateRequestEvent;
import com.delivery_signal.eureka.client.order.application.event.request.DeliveryDeleteRequest;

/**
 * 배송 관련 이벤트 발행 포트.
 * - 주문 서비스 → 배송 서비스
 */
public interface DeliveryEventPublisherPort {

    /**
     * 배송 생성 요청 이벤트 발행
     */
    void publishDeliveryCreateRequestedEvent(DeliveryCreateRequestEvent event);

    /**
     * 배송 취소 요청 이벤트 발행
     */
    void publishDeliveryCancelRequestedEvent(DeliveryDeleteRequest event);
}
