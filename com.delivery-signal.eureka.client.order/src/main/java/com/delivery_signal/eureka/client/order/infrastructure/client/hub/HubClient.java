package com.delivery_signal.eureka.client.order.infrastructure.client.hub;

import com.delivery_signal.eureka.client.order.infrastructure.client.hub.dto.StockUpdateRequestDto;
import com.delivery_signal.eureka.client.order.presentation.external.dto.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

// infrastructure.adapter.out.hub.client
@FeignClient(name = "hub-service", url = "${internal.hub.url}")
public interface HubClient {

    /**
     * 상품별 재고를 확인하는 API
     * @param productIds 상품의 id 리스트
     * @return 상품 ID별 재고 수량
     */
    @PostMapping("/open-api/v1/stocks")
    ApiResponse<Map<UUID, Integer>> getStockQuantities(@RequestBody List<UUID> productIds);

    /**
     * 허브의 상품 재고를 차감하는 API
     * @param hubId 허브 ID
     * @param requests 상품과 수량 리스트
     */
    @PostMapping("/open-api/v1/hubs/{hubId}/stocks/deduct")
    ApiResponse<Void> deductStocks(@PathVariable UUID hubId, @RequestBody List<StockUpdateRequestDto> requests);

    /**
     * 허브의 상품 재고를 복원하는 API
     * @param hubId 허브 ID
     * @param requests 상품과 수량 리스트
     */
    @PostMapping("/open-api/v1/hubs/{hubId}/stocks/restore")
    ApiResponse<Void> restoreStocks(@PathVariable UUID hubId, @RequestBody List<StockUpdateRequestDto> requests);

    /**
     * 허브의 존재 여부 확인 API
     * @param hubId 허브 ID
     * @return 허브가 존재하면 true, 존재하지 않으면 false
     */
    @GetMapping("/open-api/v1/hubs/{hubId}")
    ApiResponse<Boolean> existsById(@PathVariable UUID hubId);
}
