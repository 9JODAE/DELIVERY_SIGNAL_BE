package com.delivery_signal.eureka.client.delivery.domain.service;

import com.delivery_signal.eureka.client.delivery.domain.entity.DeliveryManager;

/**
 * 배송 배정 로직(순번 관리, 다음 담당자 조회 등)을 위한 도메인 서비스
 * Domain Layer에 위치하며, Infrastructure Layer가 이를 구현
 */
public interface DeliveryAssignmentService {
    /**
     * 다음 배송 순번에 해당하는 활성 배송 담당자를 순환 방식으로 조회
     */
    DeliveryManager getNextManagerForAssignment();
}
