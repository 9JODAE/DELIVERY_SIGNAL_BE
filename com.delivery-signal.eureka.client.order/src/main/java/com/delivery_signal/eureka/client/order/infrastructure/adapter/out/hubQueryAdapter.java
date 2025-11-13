package com.delivery_signal.eureka.client.order.infrastructure.adapter.out;

import com.delivery_signal.eureka.client.order.application.port.out.HubQueryPort;
import com.delivery_signal.eureka.client.order.infrastructure.client.hub.HubClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class hubQueryAdapter implements HubQueryPort {

    private final HubClient hubClient;

    @Override
    public Map<UUID, Integer> getStockQuantities(List<UUID> productIds) {
        return hubClient.getStockQuantities(productIds).getData();
    }

    @Override
    public boolean existsByHubId(UUID hubId) {
        return hubClient.existsById(hubId).getData();
    }
}

