package com.delivery_signal.eureka.client.order.infrastructure.adapter.out;

import com.delivery_signal.eureka.client.order.application.command.OrderProductCommand;
import com.delivery_signal.eureka.client.order.application.port.out.HubCommandPort;
import com.delivery_signal.eureka.client.order.infrastructure.client.hub.HubClient;
import com.delivery_signal.eureka.client.order.infrastructure.client.hub.dto.StockUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

/**
 * HubCommandPort 구현체
 * - Application 계층에서 받은 OrderProductCommand를 외부 DTO(StockUpdateRequestDto)로 변환 후 Feign 호출
 */
@Component
@RequiredArgsConstructor
public class HubCommandAdapter implements HubCommandPort {

    private final HubClient hubClient;

    @Override
    public void deductStocks(UUID hubId, List<OrderProductCommand> products) {
        List<StockUpdateRequestDto> requests = products.stream()
                .map(p -> StockUpdateRequestDto.builder()
                        .productId(p.getProductId())
                        .quantity(p.getQuantity())
                        .build())
                .toList();

        hubClient.deductStocks(hubId, requests);
    }

    @Override
    public void restoreStocks(UUID hubId, List<OrderProductCommand> products) {
        List<StockUpdateRequestDto> requests = products.stream()
                .map(p -> StockUpdateRequestDto.builder()
                        .productId(p.getProductId())
                        .quantity(p.getQuantity())
                        .build())
                .toList();

        hubClient.restoreStocks(hubId, requests);
    }
}
