package com.delivery_signal.eureka.client.delivery.application.service;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "hub-service", path = "/v1/hubs")
public interface HubServiceClient {
}
