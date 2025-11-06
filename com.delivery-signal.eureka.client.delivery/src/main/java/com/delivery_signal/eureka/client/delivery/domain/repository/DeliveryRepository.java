package com.delivery_signal.eureka.client.delivery.domain.repository;

import com.delivery_signal.eureka.client.delivery.domain.model.Delivery;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

// DDD 원칙: 순수한 도메인 개념만 표현
public interface DeliveryRepository {
    Delivery save(Delivery delivery);

    // 삭제되지 않은 특정 배송 정보 조회
    Optional<Delivery> findActiveById(UUID id);

    List<Delivery> findAllActive();
}