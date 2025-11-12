package com.delivery_signal.eureka.client.hub.infrastructure.external.mapper;

import com.delivery_signal.eureka.client.hub.application.dto.external.UserInfo;
import com.delivery_signal.eureka.client.hub.common.annotation.Mapper;
import com.delivery_signal.eureka.client.hub.infrastructure.external.dto.UserDTO;

@Mapper
public class UserMapper {

	public UserInfo toUserInfo(UserDTO userDTO) {
		return new UserInfo(
			userDTO.userId(),
			userDTO.role(),
			userDTO.organization(),
			userDTO.organizationId()
		);
	}
}
