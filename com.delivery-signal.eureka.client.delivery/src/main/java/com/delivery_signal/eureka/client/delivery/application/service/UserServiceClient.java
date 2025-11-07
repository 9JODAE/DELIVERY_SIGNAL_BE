package com.delivery_signal.eureka.client.delivery.application.service;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "user-service", path = "/v1/users")
public interface UserServiceClient {

}
