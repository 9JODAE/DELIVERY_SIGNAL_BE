package com.delivery_signal.eureka.client.order.domain.repository;

import com.delivery_signal.eureka.client.order.domain.entity.OrderProduct;

import java.util.List;
import java.util.UUID;

public interface OrderProductRepositoryCustom {
    List<OrderProduct> findAllByOrderId(UUID id);
}
