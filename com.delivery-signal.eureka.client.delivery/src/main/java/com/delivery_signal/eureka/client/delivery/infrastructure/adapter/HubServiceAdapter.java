package com.delivery_signal.eureka.client.delivery.infrastructure.adapter;

import com.delivery_signal.eureka.client.delivery.application.port.HubPort;
import com.delivery_signal.eureka.client.delivery.application.service.HubServiceClient;
import com.delivery_signal.eureka.client.delivery.domain.vo.HubIdentifier;
import org.springframework.stereotype.Component;

@Component
public class HubServiceAdapter implements HubPort {

    private final HubServiceClient hubServiceClient;

    public HubServiceAdapter(HubServiceClient hubServiceClient) {
        this.hubServiceClient = hubServiceClient;
    }

    @Override
    public boolean existsById(HubIdentifier hubIdentifier, Long userId, String role) {
        // Domain VO를 인프라 구현체(UUID)에 맞게 변환하여 FeignClient 호출
        return hubServiceClient.existsById(hubIdentifier.hubId(), userId, role);
    }
}
