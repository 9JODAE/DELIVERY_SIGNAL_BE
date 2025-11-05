package com.delivery_signal.eureka.client.delivery.application.service;

import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "order-service", path = "/v1/orders")
public interface OrderServiceClient {

    @GetMapping("/{order-id}")
    OrderResponse getOrderDetails(@PathVariable("order-id") Long orderId);

    public record OrderResponse(UUID id, UUID supplierCompanyId,
                                UUID receiverCompanyId,
                                UUID productId,
                                UUID deliveryId,
                                Integer transferQuantity,
                                String requestNote) {}
}
