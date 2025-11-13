package com.delivery_signal.eureka.client.company.application.port.out;

import java.util.UUID;

public interface HubQueryPort {
    boolean existsByHubId(UUID hubId);
}
