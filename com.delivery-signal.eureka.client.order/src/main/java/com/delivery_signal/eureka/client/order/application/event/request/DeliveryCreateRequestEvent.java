package com.delivery_signal.eureka.client.order.application.event.request;

import java.util.UUID;

/**
 * 배송 생성 요청 이벤트
 * - 주문이 생성되었을 때 Delivery 서비스로 전달되는 이벤트
 */
public record DeliveryCreateRequestEvent(
    Long userId,                // 배송 생성 요청자 (주문 생성자)
    String userRole,            // 요청자의 권한 (MASTER / ADMIN / ...)
    UUID orderId,               // 주문 ID
    UUID supplierCompanyId,     // 공급 업체 ID
    UUID receiverCompanyId,     // 수령 업체 ID
    UUID departureHubId,        // 출발 허브 ID (공급 업체의 허브)
    UUID destinationHubId,      // 도착 허브 ID (수령 업체의 허브)
    String address,             // 배송지 주소
    String recipient,           // 수령인 이름
    String recipientSlackId     // 수령인 Slack ID (알림용)
) {}
