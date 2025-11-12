package com.delivery_signal.eureka.client.delivery.infrastructure.repository;

import com.delivery_signal.eureka.client.delivery.domain.entity.DeliveryRouteRecords;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface JpaDeliveryRouteRecordsRepository extends JpaRepository<DeliveryRouteRecords, UUID> {

    // deletedAt이 null인 데이터만 조회
    Optional<DeliveryRouteRecords> findByRouteIdAndDeletedAtIsNull(UUID routeId);

    // 배송 ID 기준, 시퀀스 가장 큰 (마지막 경로) 기록 조회
    // 서비스 코드에서 PageRequest.of(0, 1)을 인수로 전달
    @Query("SELECT r FROM DeliveryRouteRecords r WHERE r.delivery.deliveryId = :deliveryId AND r.deletedAt IS NULL ORDER BY r.sequence DESC")
    List<DeliveryRouteRecords> findLastRecordByDeliveryId(UUID deliveryId, Pageable pageable);

    // 배송 ID 기준, updatedAt이 가장 최근인 (가장 최근 활동 기록) 기록 조회
    @Query("SELECT r FROM DeliveryRouteRecords r WHERE r.delivery.deliveryId = :deliveryId AND r.deletedAt IS NULL ORDER BY r.updatedAt DESC LIMIT 1")
    Optional<DeliveryRouteRecords> findLatestRecordByDeliveryId(UUID deliveryId);

    // 특정 배송 ID에 대한 모든 활성 경로 기록을 배송 순번대로 조회
    List<DeliveryRouteRecords> findAllByDeliveryDeliveryIdAndDeletedAtIsNullOrderBySequenceAsc(UUID deliveryDeliveryId);

    // deliveryId 기준, createdAt 오름차순으로 정렬하여 가장 처음 만들어진 기록 조회
    Optional<DeliveryRouteRecords> findTopByDelivery_DeliveryIdAndDeletedAtIsNullOrderByCreatedAtAsc(UUID deliveryId);
}
