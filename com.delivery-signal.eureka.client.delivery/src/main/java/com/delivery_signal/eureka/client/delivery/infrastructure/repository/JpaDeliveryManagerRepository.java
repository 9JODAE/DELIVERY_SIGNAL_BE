package com.delivery_signal.eureka.client.delivery.infrastructure.repository;

import com.delivery_signal.eureka.client.delivery.domain.model.DeliveryManager;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface JpaDeliveryManagerRepository extends JpaRepository<DeliveryManager, Long> {
    // Repository Interface 구현을 위한 JPA 쿼리 메서드

    // deletedAt이 null인 데이터만 조회
    Optional<DeliveryManager> findByManagerIdAndDeletedAtIsNull(Long id);

    // 가장 마지막 배송 순번
    @Query("SELECT MAX(dm.deliverySequence) FROM DeliveryManager dm WHERE dm.deletedAt IS NULL")
    Optional<Integer> findMaxDeliverySequence();
}
