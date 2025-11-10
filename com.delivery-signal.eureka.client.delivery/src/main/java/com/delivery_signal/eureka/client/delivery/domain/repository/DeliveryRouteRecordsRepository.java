package com.delivery_signal.eureka.client.delivery.domain.repository;

import com.delivery_signal.eureka.client.delivery.domain.model.DeliveryManager;
import com.delivery_signal.eureka.client.delivery.domain.model.DeliveryRouteRecords;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DeliveryRouteRecordsRepository {

    // 배송 경로(허브 이동 정보) 기록 한 번에 저장
    List<DeliveryRouteRecords> saveAll(List<DeliveryRouteRecords> routes);

    // ID에 따른 배송 경로 기록 조회
    Optional<DeliveryRouteRecords> findActiveById(UUID routeId);
}
