package com.delivery_signal.eureka.client.order.infrastructure.client.hub;

import com.delivery_signal.eureka.client.order.infrastructure.client.hub.dto.StockUpdateRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping("/open-api/v1/hubs/stocks")
    Map<UUID, Integer> getStockQuantities(@RequestBody List<UUID> productIds);

    /**
     * 허브의 상품재고를 줄이는 api
     *  List에 포함될 내용
     *  UUID productId;
     *  int quantity;
     *
     * @param requests 상품과 수량 리스트
     */
    @PostMapping("/open-api/v1/hubs/stocks/decrease")
    void decreaseStock(@RequestBody List<StockUpdateRequestDto> requests);

    /**
     * 허브의 상품재고를 복원하는 api
     *  List에 포함될 내용
     *  UUID productId;
     *  int quantity;
     *
     * @param requests 상품과 수량 리스트
     */
    @PostMapping("/open-api/v1/hubs/stocks/restore")
    void restoreStock(@RequestBody List<StockUpdateRequestDto> requests);

    /**
     * 허브의 유효성을 검증하는 api
     * @param uuid 허브id
     * @return 유효하면 true, 유효하지 않으면 예외처리
     */
    @GetMapping("/open-api/v1/hubs/{hubId}")
    boolean existsById(@RequestParam("hubId") UUID uuid);
}
