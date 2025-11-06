package com.delivery_signal.eureka.client.delivery.domain.repository;

import com.delivery_signal.eureka.client.delivery.domain.model.Delivery;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DeliveryRepository extends JpaRepository<Delivery, UUID> {
    // deletedAt이 null인 데이터만 조회
    Optional<Delivery> findByDeliveryIdAndDeletedAtIsNull(UUID deliveryId);

    // deletedAt이 null인(삭제되지 않은) 데이터만 조회하도록 기본 메서드를 오버라이드
//    @Override
//    @Query("select d from Delivery d where d.deletedAt is null")
//    List<Delivery> findAll();


}
