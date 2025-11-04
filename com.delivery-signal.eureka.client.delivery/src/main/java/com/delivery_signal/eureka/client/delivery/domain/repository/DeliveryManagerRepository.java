package com.delivery_signal.eureka.client.delivery.domain.repository;

import com.delivery_signal.eureka.client.delivery.domain.model.DeliveryManager;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryManagerRepository extends JpaRepository<DeliveryManager, Long> {
    // deletedAt이 null인 데이터만 조회
    Optional<DeliveryManager> findByManagerIdAndDeletedAtIsNull(Long id);
}
