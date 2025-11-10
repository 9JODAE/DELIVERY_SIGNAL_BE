package com.delivery_signal.eureka.client.order.application.port.out;

import com.delivery_signal.eureka.client.order.domain.vo.hub.HubStockInfo;

import java.util.List;
import java.util.UUID;


public interface HubQueryPort {
    List<HubStockInfo> getStockQuantities(List<UUID> productIds);
}
