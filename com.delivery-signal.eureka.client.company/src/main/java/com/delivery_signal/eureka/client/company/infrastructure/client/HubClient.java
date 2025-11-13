package com.delivery_signal.eureka.client.company.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "hub-service", url = "${internal.hub.url}")
public interface HubClient {

    /**
     * 허브의 존재 여부 확인 API
     * @param hubId 허브 ID
     * @return 허브가 존재하면 true, 존재하지 않으면 false
     */
    @GetMapping("/open-api/v1/hubs/{hubId}")
    boolean existsById(@PathVariable UUID hubId);
}
