package com.delivery_signal.eureka.client.order.infrastructure.adapter.out;

import com.delivery_signal.eureka.client.order.application.port.out.HubCommandPort;
import com.delivery_signal.eureka.client.order.infrastructure.client.hub.HubClient;
import com.delivery_signal.eureka.client.order.infrastructure.client.hub.dto.StockUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
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
    public void deductStocks(UUID hubId, Map<UUID, Integer> products) {
        StockUpdateRequestDto request = StockUpdateRequestDto.builder()
                .products(products)
                .build();

        hubClient.deductStocks(hubId, request);
    }

    @Override
    public void restoreStocks(UUID hubId, Map<UUID, Integer> products) {
        StockUpdateRequestDto request = StockUpdateRequestDto.builder()
                .products(products)
                .build();

        hubClient.restoreStocks(hubId, request);
    }
}
