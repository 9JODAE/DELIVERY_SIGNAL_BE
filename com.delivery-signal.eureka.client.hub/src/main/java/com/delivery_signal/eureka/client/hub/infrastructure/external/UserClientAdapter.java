package com.delivery_signal.eureka.client.hub.infrastructure.external;

import com.delivery_signal.eureka.client.hub.application.dto.external.UserInfo;
import com.delivery_signal.eureka.client.hub.application.port.UserClient;
import com.delivery_signal.eureka.client.hub.common.annotation.ExternalAdapter;
import com.delivery_signal.eureka.client.hub.common.api.ApiResponse;
import com.delivery_signal.eureka.client.hub.infrastructure.external.dto.UserDTO;
import com.delivery_signal.eureka.client.hub.infrastructure.external.mapper.UserMapper;

import lombok.RequiredArgsConstructor;

@ExternalAdapter
@RequiredArgsConstructor
public class UserClientAdapter implements UserClient {
	private final UserFeignClient userFeignClient;
	private final UserMapper userMapper;

	@Override
	public UserInfo getUserInfo(String userId) {
		ApiResponse<UserDTO> response = userFeignClient.authorization(userId);
		return userMapper.toUserInfo(response.data());
	}
}
