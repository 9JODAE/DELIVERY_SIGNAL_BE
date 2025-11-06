package com.delivery_signal.eureka.client.delivery.infrastructure.repository;

import com.delivery_signal.eureka.client.delivery.domain.model.DeliveryManager;
import com.delivery_signal.eureka.client.delivery.domain.repository.DeliveryManagerRepository;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class DeliveryManagerRepositoryImpl implements DeliveryManagerRepository {

    // JpaRepository를 주입받아 사용 (어댑터 역할)
    private final JpaDeliveryManagerRepository jpaRepository;

    public DeliveryManagerRepositoryImpl(JpaDeliveryManagerRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public DeliveryManager save(DeliveryManager manager) {
        return jpaRepository.save(manager);
    }

    @Override
    public Optional<DeliveryManager> findActiveById(Long id) {
        return jpaRepository.findByManagerIdAndDeletedAtIsNull(id);
    }
}
