package com.delivery_signal.eureka.client.order.application.port.out;

import com.delivery_signal.eureka.client.order.application.command.OrderProductCommand;
import java.util.List;
import java.util.UUID;

/**
 * 허브(Hub) 재고 관련 커맨드 포트 (Outbound)
 * Application 계층에서 외부 허브로 재고 차감/복구 요청을 추상화
 */
public interface HubCommandPort {

    void deductStocks(UUID hubId, List<OrderProductCommand> products);
    void restoreStocks(UUID hubId, List<OrderProductCommand> products);
}