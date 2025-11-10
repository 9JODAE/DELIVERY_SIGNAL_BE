package com.delivery_signal.eureka.client.delivery.infrastructure.repository;

import com.delivery_signal.eureka.client.delivery.domain.model.Delivery;
import com.delivery_signal.eureka.client.delivery.domain.repository.DeliveryRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class DeliveryRepositoryImpl implements DeliveryRepository {

    private final JpaDeliveryRepository jpaRepository;

    public DeliveryRepositoryImpl(JpaDeliveryRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Delivery save(Delivery delivery) {
        return jpaRepository.save(delivery);
    }

    @Override
    public Optional<Delivery> findActiveById(UUID id) {
        return jpaRepository.findByDeliveryIdAndDeletedAtIsNull(id);
    }

    @Override
    public List<Delivery> findAllActive() {
        return jpaRepository.findAllByDeletedAtIsNull();
    }

    @Override
    public Page<Delivery> findActivePageByManagerId(Long managerId, Pageable pageable) {
        return jpaRepository.findAllByDeliveryManagerIdAndDeletedAtIsNull(managerId, pageable);
    }
}
