package com.delivery_signal.eureka.client.delivery.infrastructure.repository;

import com.delivery_signal.eureka.client.delivery.domain.model.DeliveryManager;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JpaDeliveryManagerRepository extends JpaRepository<DeliveryManager, Long> {
    // Repository Interface 구현을 위한 JPA 쿼리 메서드

    // deletedAt이 null인 데이터만 조회
    Optional<DeliveryManager> findByManagerIdAndDeletedAtIsNull(Long id);

    // 가장 마지막 배송 순번
    @Query("SELECT MAX(dm.deliverySequence) FROM DeliveryManager dm WHERE dm.deletedAt IS NULL")
    Optional<Integer> findMaxDeliverySequence();

    // 다음 순번에 해당하는 활성 담당자를 찾는 쿼리 (순환 로직 처리)
//    @Query(value = """
//    SELECT * FROM delivery_manager dm
//    WHERE dm.deleted_at IS NULL
//    AND dm.delivery_sequence >= :nextSequenceToFind
//    ORDER BY dm.delivery_sequence ASC
//    LIMIT 1
//    """, nativeQuery = true)
//    Optional<DeliveryManager> findNextActiveManagerFromSequence(@Param("nextSequenceToFind") int nextSequenceToFind);

    // 삭제되지 않은 배송 담당자 수
    Long countByDeletedAtIsNull();

    // lastSequence보다 크면서 가장 작은 순번(바로 다음 순번)을 가진 담당자 조회
    @Query(value = "SELECT dm.* FROM p_delivery_managers dm WHERE dm.deleted_at IS NULL AND dm.sequence > :lastSequence ORDER BY dm.sequence ASC LIMIT 1", nativeQuery = true)
    Optional<DeliveryManager> findNextBySequenceGreaterThan(@Param("lastSequence") int lastSequence);

    // 다음 배송 담당자가 없으면 순번 0부터 시작하는 가장 작은 순번의 담당자 조회
    @Query(value = "SELECT dm.* FROM p_delivery_managers dm WHERE dm.deleted_at IS NULL ORDER BY dm.sequence ASC LIMIT 1", nativeQuery = true)
    Optional<DeliveryManager> findFirstActiveManager();
}
