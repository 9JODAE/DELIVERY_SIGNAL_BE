package com.delivery_signal.eureka.client.order.application.port.out;

import com.delivery_signal.eureka.client.order.application.result.OrderForDeliveryResult;

import java.util.Optional;
import java.util.UUID;

/**
 * 배송 관련 조회 포트
 * - 주문/배송 조회 담당
 */
public interface DeliveryQueryPort {
    Optional<OrderForDeliveryResult> findOrderForDeliveryById(UUID orderId);
}