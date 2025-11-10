package com.delivery_signal.eureka.client.delivery.domain.repository;

import com.delivery_signal.eureka.client.delivery.domain.model.DeliveryRouteRecords;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

public interface DeliveryRouteRecordsRepository {

    // 배송 경로(허브 이동 정보) 기록 한 번에 저장
    List<DeliveryRouteRecords> saveAll(List<DeliveryRouteRecords> routes);

    // ID에 따른 배송 경로 기록 조회
    Optional<DeliveryRouteRecords> findActiveById(UUID routeId);

    // 특정 배송의 가장 마지막 시퀀스 번호를 가진 (최종) 경로 기록을 조회
    List<DeliveryRouteRecords> findLastRouteRecord(UUID deliveryId, Pageable pageable);

    // 특정 배송의 가장 최근 상태 기록을 조회
    Optional<DeliveryRouteRecords> findLatestRouteRecord(UUID deliveryId);
}
