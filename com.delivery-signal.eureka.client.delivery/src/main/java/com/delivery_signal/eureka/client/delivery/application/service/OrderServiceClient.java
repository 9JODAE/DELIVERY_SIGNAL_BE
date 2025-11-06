package com.delivery_signal.eureka.client.delivery.application.service;

import java.time.Instant;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "order-service", path = "/v1/orders")
public interface OrderServiceClient {

    @GetMapping
    OrderPongResponseDto ping(@RequestParam(required = false) String from);

    record OrderPongResponseDto(
        String toOrder,
        String status,
        Instant timestamp
    ) {}
}
