package com.delivery_signal.eureka.client.order.application.service.external;

import com.delivery_signal.eureka.client.order.domain.vo.ArrivalHubInfo;
import com.delivery_signal.eureka.client.order.domain.vo.DepartureHubInfo;

import java.util.UUID;

public interface HubService {

    DepartureHubInfo getDepartureHub(UUID departureHubId);

    ArrivalHubInfo getArrivalHub(UUID arrivalHubId);
}
