package com.delivery_signal.eureka.client.order.infrastructure.adapter.out;

import com.delivery_signal.eureka.client.order.application.port.out.HubQueryPort;
import com.delivery_signal.eureka.client.order.domain.vo.hub.HubStockInfo;
import com.delivery_signal.eureka.client.order.infrastructure.client.hub.HubClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

// infrastructure.adapter.out.hub
@Component
@RequiredArgsConstructor
public class hubQueryAdapter implements HubQueryPort {

    private final HubClient hubClient;

    @Override
    public List<HubStockInfo> getStockQuantities(List<UUID> productIds) {
        return hubClient.getStockQuantities(productIds);
    }
}

