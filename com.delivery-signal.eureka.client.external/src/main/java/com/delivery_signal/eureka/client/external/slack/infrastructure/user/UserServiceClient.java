package com.delivery_signal.eureka.client.external.slack.infrastructure.user;

import com.delivery_signal.eureka.client.external.slack.infrastructure.user.dto.UserAuthorizationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service", url = "http://localhost/opne-api/v1/auth")
public interface UserServiceClient {

    @GetMapping("authorization")
    UserAuthorizationResponse getUserAuthorizationInfo(@RequestParam("userId") Long userId);

}