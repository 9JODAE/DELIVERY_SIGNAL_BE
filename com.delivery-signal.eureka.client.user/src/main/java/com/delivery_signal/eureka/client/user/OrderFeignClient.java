package com.delivery_signal.eureka.client.user;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name="order-service")
public interface OrderFeignClient {
    @GetMapping("/order/temp")
    String getOrder();
}
