package com.delivery_signal.eureka.client.delivery.infrastructure.repository;

import com.delivery_signal.eureka.client.delivery.domain.model.DeliveryRouteRecords;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaDeliveryRouteRecordsRepository extends JpaRepository<DeliveryRouteRecords, UUID> {
}
