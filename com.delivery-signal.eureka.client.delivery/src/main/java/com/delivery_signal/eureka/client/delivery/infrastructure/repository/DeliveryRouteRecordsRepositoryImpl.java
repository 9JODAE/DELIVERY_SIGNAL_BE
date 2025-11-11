package com.delivery_signal.eureka.client.delivery.infrastructure.repository;

import com.delivery_signal.eureka.client.delivery.domain.entity.DeliveryRouteRecords;
import com.delivery_signal.eureka.client.delivery.domain.repository.DeliveryRouteRecordsRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
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

    @Override
    public List<DeliveryRouteRecords> findLastRouteRecord(UUID deliveryId, Pageable pageable) {
        return jpaRepository.findLastRecordByDeliveryId(deliveryId, pageable);
    }

    @Override
    public Optional<DeliveryRouteRecords> findLatestRouteRecord(UUID deliveryId) {
        return jpaRepository.findLatestRecordByDeliveryId(deliveryId);
    }

    @Override
    public List<DeliveryRouteRecords> findAllByDeliveryIdOrderBySequence(UUID deliveryId) {
        return jpaRepository.findAllByDeliveryDeliveryIdAndDeletedAtIsNullOrderBySequenceAsc(deliveryId);
    }
}
