package com.delivery_signal.eureka.client.company.application.service;

import com.delivery_signal.eureka.client.company.application.port.out.OrderProductQueryPort;
import com.delivery_signal.eureka.client.company.application.result.OrderProductResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Order 서비스 통신용 Internal Product Service
 */
@Service
@RequiredArgsConstructor
public class InternalOrderProductService {

    private final OrderProductQueryPort orderProductQueryPort;

    public List<OrderProductResult> getProducts(List<UUID> productIds) {
        return orderProductQueryPort.findProductsByIds(productIds);
    }
}

