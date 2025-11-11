package com.delivery_signal.eureka.client.delivery.domain.repository;

import com.delivery_signal.eureka.client.delivery.application.dto.DeliverySearchCondition;
import com.delivery_signal.eureka.client.delivery.common.UserRole;
import com.delivery_signal.eureka.client.delivery.domain.entity.Delivery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DeliveryQueryRepository {
    /**
     * 배송 목록 검색
     */
    Page<Delivery> searchDeliveries(Long currUserId, DeliverySearchCondition condition, Pageable pageable, UserRole userRole);
}
