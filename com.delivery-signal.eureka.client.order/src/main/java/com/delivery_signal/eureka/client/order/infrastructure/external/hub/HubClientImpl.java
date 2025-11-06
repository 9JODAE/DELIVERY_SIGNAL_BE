package com.delivery_signal.eureka.client.order.infrastructure.external.hub;

import com.delivery_signal.eureka.client.order.application.service.external.HubService;
import com.delivery_signal.eureka.client.order.domain.vo.ArrivalHubInfo;
import com.delivery_signal.eureka.client.order.domain.vo.DepartureHubInfo;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class HubClientImpl implements HubService {

    private final HubClient hubClient; // FeignClient

    public HubClientImpl(HubClient hubClient) {
        this.hubClient = hubClient;
    }

    @Override
    public DepartureHubInfo getDepartureHub(UUID departureHubId) {
        return hubClient.getDepartureHub(departureHubId);
    }

    @Override
    public ArrivalHubInfo getArrivalHub(UUID arrivalHubId) {
        return hubClient.getArrivalHub(arrivalHubId);
    }
}
