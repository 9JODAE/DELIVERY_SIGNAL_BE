package com.delivery_signal.eureka.client.delivery.domain.repository;

import com.delivery_signal.eureka.client.delivery.domain.model.DeliveryManager;
import java.util.Optional;

public interface DeliveryManagerRepository {
    /**
     * DDD 원칙
     * 순수한 도메인 개념만 표현
     * 구현체는 인프라스트럭처 계층에서 제공
     */

    /**
     * 배송 담당자 등록
     * @param manager
     * @return
     */
    DeliveryManager save(DeliveryManager manager);

    /**
     * 배송 담당자 정보 조회 - deletedAt이 null인 데이터만 조회
     * @param id
     * @return
     */
    Optional<DeliveryManager> findActiveById(Long id);

    /**
     * 마지막 배송 순번 조회
     * @return
     */
    Optional<Integer> findMaxActiveSequence();
}
