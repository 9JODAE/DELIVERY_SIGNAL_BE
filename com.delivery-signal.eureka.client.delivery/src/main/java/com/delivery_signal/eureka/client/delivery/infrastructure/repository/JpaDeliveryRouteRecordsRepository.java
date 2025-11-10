package com.delivery_signal.eureka.client.delivery.infrastructure.repository;

import com.delivery_signal.eureka.client.delivery.domain.model.DeliveryManager;
import com.delivery_signal.eureka.client.delivery.domain.model.DeliveryRouteRecords;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaDeliveryRouteRecordsRepository extends JpaRepository<DeliveryRouteRecords, UUID> {

    // deletedAt이 null인 데이터만 조회
    Optional<DeliveryRouteRecords> findByRouteIdAndDeletedAtIsNull(UUID routeId);
}
