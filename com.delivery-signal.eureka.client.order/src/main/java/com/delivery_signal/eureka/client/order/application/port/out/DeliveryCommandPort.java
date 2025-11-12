package com.delivery_signal.eureka.client.order.application.port.out;

import com.delivery_signal.eureka.client.order.application.command.CreateDeliveryCommand;
import com.delivery_signal.eureka.client.order.domain.vo.delivery.DeliveryCreatedInfo;
/**
 * 배송 관련 커맨드 포트
 * - 생성/취소 담당
 */
public interface DeliveryCommandPort {
    DeliveryCreatedInfo createDelivery(CreateDeliveryCommand command);
    void cancelDelivery(java.util.UUID deliveryId);
}
