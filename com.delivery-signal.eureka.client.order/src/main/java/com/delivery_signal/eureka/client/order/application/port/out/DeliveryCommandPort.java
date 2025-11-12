package com.delivery_signal.eureka.client.order.application.port.out;

import com.delivery_signal.eureka.client.order.application.command.CreateDeliveryCommand;
import com.delivery_signal.eureka.client.order.domain.vo.delivery.DeliveryCreatedInfo;
import com.delivery_signal.eureka.client.order.application.result.OrderForDeliveryResult;

import java.util.Optional;
import java.util.UUID;

public interface DeliveryCommandPort {
    Optional<OrderForDeliveryResult> findOrderForDeliveryById(UUID orderId);
    DeliveryCreatedInfo createDelivery(CreateDeliveryCommand deliveryRequest);
    void cancelDelivery(UUID deliveryId);
}
