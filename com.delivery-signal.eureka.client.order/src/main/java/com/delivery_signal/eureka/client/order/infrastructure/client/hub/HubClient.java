package com.delivery_signal.eureka.client.order.infrastructure.client.hub;

import com.delivery_signal.eureka.client.order.infrastructure.client.hub.dto.StockUpdateRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;
import java.util.UUID;

// infrastructure.adapter.out.hub.client
@FeignClient(name = "hub-service", url = "${internal.hub.url}")
public interface HubClient {

    /**
     * 상품별 재고를 확인하는 api
     * @param productIds 상품의 id리스트
     * @return 상품과 재고의 List
     */
    @PostMapping("open-api/v1/hubs/stocks")
    Map<UUID, Integer> getStockQuantities(@RequestBody List<UUID> productIds);

    @PostMapping("/stock/decrease")
    void decreaseStock(@RequestBody List<StockUpdateRequestDto> requests);

    @PostMapping("/stock/restore")
    void restoreStock(@RequestBody List<StockUpdateRequestDto> requests);
}
