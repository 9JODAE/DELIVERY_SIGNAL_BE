package com.delivery_signal.eureka.client.order.application.port.out;

import com.delivery_signal.eureka.client.order.application.command.OrderProductCommand;
import java.util.List;

/**
 * 허브(Hub) 재고 관련 커맨드 포트 (Outbound)
 * Application 계층에서 외부 허브로 재고 차감/복구 요청을 추상화
 * Application은 DTO(OrderProductCommand)만 알고, 외부 DTO 변환은 Adapter에서 처리
 */
public interface HubCommandPort {
    void decreaseStock(List<OrderProductCommand> products);
    void restoreStock(List<OrderProductCommand> products); // 주문 취소용
}