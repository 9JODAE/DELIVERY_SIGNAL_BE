package com.delivery_signal.eureka.client.order.infrastructure.client.delivery;

import com.delivery_signal.eureka.client.order.domain.vo.delivery.DeliveryCreatedInfo;
import com.delivery_signal.eureka.client.order.infrastructure.client.ApiResponseDto;
import com.delivery_signal.eureka.client.order.infrastructure.client.delivery.dto.DeliveryCreateRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@FeignClient(name = "delivery-service", url = "${internal.delivery.url}")
public interface DeliveryClient {

    /**
     * 배송 생성 요청
     */
    @PostMapping
    ApiResponseDto<DeliveryCreatedInfo> createDelivery(@RequestBody DeliveryCreateRequestDto request);

    /**
     * 배송 취소 요청 (논리 삭제)
     */
    @DeleteMapping("/open-api/v1/deliveries/{delivery-id}")
    ApiResponseDto<Void> cancelDelivery(
            @PathVariable("delivery-id") UUID deliveryId,
            @RequestHeader("X-User-Id") Long userId,
            @RequestHeader("X-User-Role") String role
    );
}
