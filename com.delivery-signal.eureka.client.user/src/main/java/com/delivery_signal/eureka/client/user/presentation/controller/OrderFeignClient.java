package com.delivery_signal.eureka.client.user.presentation.controller;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name="order-service")
public interface OrderFeignClient {
    @GetMapping("/order/temp")
    String getOrder();
}
