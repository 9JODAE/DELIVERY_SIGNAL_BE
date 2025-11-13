package com.delivery_signal.eureka.client.delivery.infrastructure.repository;

import com.delivery_signal.eureka.client.delivery.domain.entity.DeliveryManager;
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

    @Override
    public Optional<Integer> findMaxActiveSequence() {
        return jpaRepository.findMaxDeliverySequence();
    }

    @Override
    public Long countActiveManagers() {
        return jpaRepository.countByDeletedAtIsNull();
    }

    @Override
    public Optional<DeliveryManager> findNextActiveManager(int lastSequence) {

        Optional<DeliveryManager> nextManager = jpaRepository.findNextBySequenceGreaterThan(
            lastSequence);

        if (nextManager.isPresent()) {
            return nextManager;
        }

        // 다음 배정된 배송 담당자가 없으면 순환 (다시 순번 0부터 시작)
        return jpaRepository.findFirstActiveManager();
    }
}
