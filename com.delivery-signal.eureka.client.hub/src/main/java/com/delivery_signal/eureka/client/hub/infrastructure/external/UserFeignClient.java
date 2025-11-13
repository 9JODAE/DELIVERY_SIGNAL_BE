package com.delivery_signal.eureka.client.hub.infrastructure.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import com.delivery_signal.eureka.client.hub.infrastructure.external.dto.UserDTO;
import com.delivery_signal.eureka.client.hub.common.api.ApiResponse;

@FeignClient(name = "user-service", path = "/open-api/v1/auth")
public interface UserFeignClient {

	@GetMapping("/authorization")
	ApiResponse<UserDTO> authorization(@RequestHeader("x-user-id") String userId);

}
