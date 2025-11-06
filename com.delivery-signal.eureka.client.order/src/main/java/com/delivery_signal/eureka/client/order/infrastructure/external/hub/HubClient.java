package com.delivery_signal.eureka.client.order.infrastructure.external.hub;

import com.delivery_signal.eureka.client.order.domain.vo.ArrivalHubInfo;
import com.delivery_signal.eureka.client.order.domain.vo.DepartureHubInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "hub-service", url = "${external.hub.url}")
public interface HubClient {

    @GetMapping("/hubs/arrival/{id}")
    ArrivalHubInfo getArrivalHub(@PathVariable UUID id);

    @GetMapping("/hubs/departure/{id}")
    DepartureHubInfo getDepartureHub(@PathVariable UUID id);
}