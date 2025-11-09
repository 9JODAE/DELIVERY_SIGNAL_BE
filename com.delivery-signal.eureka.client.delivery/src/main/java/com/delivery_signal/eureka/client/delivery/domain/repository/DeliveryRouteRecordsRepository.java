package com.delivery_signal.eureka.client.delivery.domain.repository;

import com.delivery_signal.eureka.client.delivery.domain.model.DeliveryRouteRecords;
import java.util.List;

public interface DeliveryRouteRecordsRepository {

    // 배송 경로(허브 이동 정보) 기록 한 번에 저장
    List<DeliveryRouteRecords> saveAll(List<DeliveryRouteRecords> routes);
}
