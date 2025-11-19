package com.delivery_signal.eureka.client.order.application.port.out;


import java.util.Map;
import java.util.UUID;

/**
 * 허브(Hub) 재고 관련 커맨드 포트 (Outbound)
 * Application 계층에서 외부 허브로 재고 차감/복구 요청을 추상화
 */
public interface HubCommandPort {

    void deductStocks(UUID hubId, Map<UUID, Integer> products);
    void restoreStocks(UUID hubId, Map<UUID, Integer>  products);
}