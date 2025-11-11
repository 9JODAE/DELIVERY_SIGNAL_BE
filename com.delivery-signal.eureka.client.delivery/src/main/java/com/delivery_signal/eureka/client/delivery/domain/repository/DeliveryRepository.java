package com.delivery_signal.eureka.client.delivery.domain.repository;

import com.delivery_signal.eureka.client.delivery.domain.entity.Delivery;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

// DDD 원칙: 순수한 도메인 개념만 표현
public interface DeliveryRepository {
    Delivery save(Delivery delivery);

    // 삭제되지 않은 특정 배송 정보 조회
    Optional<Delivery> findActiveById(UUID id);

    List<Delivery> findAllActive();

    // 특정 담당자에게 할당된 활성 배송 목록을 페이징하여 조회
    Page<Delivery> findActivePageByManagerId(Long managerId, Pageable pageable);
}