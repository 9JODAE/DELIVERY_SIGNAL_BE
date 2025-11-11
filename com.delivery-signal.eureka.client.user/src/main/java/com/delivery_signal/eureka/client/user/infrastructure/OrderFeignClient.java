package com.delivery_signal.eureka.client.user.infrastructure;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name="order-service")
public interface OrderFeignClient {
    @GetMapping("/open-api/v1/orders")
    String test();
}
