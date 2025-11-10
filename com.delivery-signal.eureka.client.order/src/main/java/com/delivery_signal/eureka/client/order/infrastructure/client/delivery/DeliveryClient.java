package com.delivery_signal.eureka.client.order.infrastructure.client.delivery;

import com.delivery_signal.eureka.client.order.domain.vo.delivery.DeliveryCreatedInfo;
import com.delivery_signal.eureka.client.order.infrastructure.client.delivery.dto.DeliveryCreateRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "delivery-service", url = "${internal.delivery.url}")
public interface DeliveryClient {

    @PostMapping("/open-api/v1/deliveries")
    DeliveryCreatedInfo createDelivery(@RequestBody DeliveryCreateRequestDto request);
}
