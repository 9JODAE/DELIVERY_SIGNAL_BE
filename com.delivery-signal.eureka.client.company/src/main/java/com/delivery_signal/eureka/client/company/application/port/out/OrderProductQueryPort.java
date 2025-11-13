package com.delivery_signal.eureka.client.company.application.port.out;

import com.delivery_signal.eureka.client.company.application.result.OrderProductResult;

import java.util.List;
import java.util.UUID;

public interface OrderProductQueryPort {
    List<OrderProductResult> findProductsByIds(List<UUID> productIds);
}