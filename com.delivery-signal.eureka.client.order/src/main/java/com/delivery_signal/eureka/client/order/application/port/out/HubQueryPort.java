package com.delivery_signal.eureka.client.order.application.port.out;

import java.util.List;
import java.util.Map;
import java.util.UUID;


public interface HubQueryPort {
    Map<UUID, Integer> getStockQuantities(List<UUID> productIds);
    boolean existsByHubId(UUID hubId);
}
