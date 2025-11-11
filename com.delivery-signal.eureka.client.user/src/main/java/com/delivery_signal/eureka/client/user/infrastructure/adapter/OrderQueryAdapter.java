package com.delivery_signal.eureka.client.user.infrastructure.adapter;

import com.delivery_signal.eureka.client.user.application.port.out.OrderQueryPort;
import com.delivery_signal.eureka.client.user.infrastructure.OrderFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderQueryAdapter implements OrderQueryPort {

    private final OrderFeignClient orderFeignClient;

    @Override
    public String getOrder() {
        return orderFeignClient.getOrder();
    }
}
