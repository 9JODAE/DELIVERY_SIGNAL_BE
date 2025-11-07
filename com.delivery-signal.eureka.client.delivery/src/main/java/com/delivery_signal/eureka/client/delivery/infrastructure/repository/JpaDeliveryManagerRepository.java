package com.delivery_signal.eureka.client.delivery.infrastructure.repository;

import com.delivery_signal.eureka.client.delivery.domain.model.DeliveryManager;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaDeliveryManagerRepository extends JpaRepository<DeliveryManager, Long> {
    // Repository Interface 구현을 위한 JPA 쿼리 메서드

    // deletedAt이 null인 데이터만 조회
    Optional<DeliveryManager> findByManagerIdAndDeletedAtIsNull(Long id);
}
