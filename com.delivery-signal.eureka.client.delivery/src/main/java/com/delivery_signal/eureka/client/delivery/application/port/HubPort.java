package com.delivery_signal.eureka.client.delivery.application.port;

import com.delivery_signal.eureka.client.delivery.domain.vo.HubIdentifier;
import com.delivery_signal.eureka.client.delivery.domain.vo.HubRouteInfo;
import java.util.List;

/**
 * Port: Delivery Domain이 Hub로부터 허브의 유효성을 검증받는 추상화 인터페이스 (Output Port)
 */
public interface HubPort {
    /**
     * 특정 허브 ID가 현재 시스템에 존재하는지 확인 (HubIdentifier: 도메인 VO)
     */
    boolean existsById(HubIdentifier hubIdentifier, Long userId, String role);

    /**
     * 허브 간 배송 경로 목록 조회
     * @param departureId 출발 허브 ID
     * @param arrivalId 도착 허브 ID
     * @return 허브 경로 단계별 정보 목록 (VO)
     */
    List<HubRouteInfo> searchRoutes(HubIdentifier departureId, HubIdentifier arrivalId);
}
