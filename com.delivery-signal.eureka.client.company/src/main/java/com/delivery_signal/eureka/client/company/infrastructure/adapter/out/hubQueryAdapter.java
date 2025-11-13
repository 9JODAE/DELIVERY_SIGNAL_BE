package com.delivery_signal.eureka.client.company.infrastructure.adapter.out;

import com.delivery_signal.eureka.client.company.application.port.out.HubQueryPort;
import com.delivery_signal.eureka.client.company.infrastructure.client.HubClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class hubQueryAdapter implements HubQueryPort {

    private final HubClient hubClient;

    @Override
    public boolean existsByHubId(UUID hubId) {
        return hubClient.existsById(hubId);
    }
}
