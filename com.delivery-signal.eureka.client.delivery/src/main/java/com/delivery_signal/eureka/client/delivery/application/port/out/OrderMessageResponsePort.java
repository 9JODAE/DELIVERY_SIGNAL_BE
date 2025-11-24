package com.delivery_signal.eureka.client.delivery.application.port.out;

import com.delivery_signal.eureka.client.delivery.application.dto.event.DeliveryCreatedRequestEvent;

/**
 * [Outbound Port]
 * 배송 생성 완료 후 Order-Service로 결과를 응답하는 포트 (이벤트 발행)
 */
public interface OrderMessageResponsePort {
    void sendDeliveryCreatedResponse(DeliveryCreatedRequestEvent event);
}
