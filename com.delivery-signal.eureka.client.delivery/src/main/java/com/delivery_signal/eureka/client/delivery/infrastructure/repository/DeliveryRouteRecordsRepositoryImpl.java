package com.delivery_signal.eureka.client.delivery.infrastructure.repository;

import com.delivery_signal.eureka.client.delivery.domain.model.DeliveryRouteRecords;
import com.delivery_signal.eureka.client.delivery.domain.repository.DeliveryRouteRecordsRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class DeliveryRouteRecordsRepositoryImpl implements DeliveryRouteRecordsRepository {

    private final JpaDeliveryRouteRecordsRepository jpaRepository;

    public DeliveryRouteRecordsRepositoryImpl(JpaDeliveryRouteRecordsRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<DeliveryRouteRecords> saveAll(List<DeliveryRouteRecords> routes) {
        return jpaRepository.saveAll(routes);
    }

    @Override
    public Optional<DeliveryRouteRecords> findActiveById(UUID routeId) {
        return jpaRepository.findByRouteIdAndDeletedAtIsNull(routeId);
    }
}
