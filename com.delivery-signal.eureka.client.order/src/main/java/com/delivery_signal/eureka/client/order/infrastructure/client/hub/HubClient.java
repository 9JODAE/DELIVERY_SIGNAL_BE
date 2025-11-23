package com.delivery_signal.eureka.client.order.infrastructure.client.hub;

import com.delivery_signal.eureka.client.order.infrastructure.client.hub.dto.GetStockQuantitiesRequestDto;
import com.delivery_signal.eureka.client.order.infrastructure.client.hub.dto.StockUpdateRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;
import java.util.UUID;

// infrastructure.adapter.out.hub.client
@FeignClient(name = "hub-service", url = "${internal.hub.url}")
public interface HubClient {

    /**
     * 상품별 재고를 확인하는 API
     * @param request 상품의 id 리스트
     * @return 상품 ID별 재고 수량
     */
    @PostMapping("/open-api/v1/stocks")
    Map<UUID, Integer> getStockQuantities(@RequestBody GetStockQuantitiesRequestDto request);

    /**
     * 허브의 상품 재고를 차감하는 API
     * @param hubId 허브 ID
     * @param requests 상품과 수량 리스트
     */
    @PostMapping("/open-api/v1/hubs/{hubId}/stocks/deduct")
    Void deductStocks(@PathVariable UUID hubId, @RequestBody StockUpdateRequestDto requests);

    /**
     * 허브의 상품 재고를 복원하는 API
     * @param hubId 허브 ID
     * @param requests 상품과 수량 리스트
     */
    @PostMapping("/open-api/v1/hubs/{hubId}/stocks/restore")
    Void restoreStocks(@PathVariable UUID hubId, @RequestBody StockUpdateRequestDto requests);

    /**
     * 허브의 존재 여부 확인 API
     * @param hubId 허브 ID
     * @return 허브가 존재하면 true, 존재하지 않으면 false
     */
    @GetMapping("/open-api/v1/hubs/{hubId}")
    Boolean existsById(@PathVariable UUID hubId);
}
