package com.delivery_signal.eureka.client.hub.common.aop;

import java.util.Arrays;
import java.util.List;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import com.delivery_signal.eureka.client.hub.application.dto.external.UserInfo;
import com.delivery_signal.eureka.client.hub.application.port.UserClient;
import com.delivery_signal.eureka.client.hub.common.annotation.PreAuthorize;
import com.delivery_signal.eureka.client.hub.common.auth.Authority;
import com.delivery_signal.eureka.client.hub.common.auth.UserContextHolder;
import com.delivery_signal.eureka.client.hub.common.error.HubErrorCode;
import com.delivery_signal.eureka.client.hub.common.exception.UnauthorizedException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class AuthorizationAspect {

	private final UserClient userClient;

	@Before("@annotation(preAuthorize)")
	public void checkAuthority(PreAuthorize preAuthorize) {
		String userId = UserContextHolder.getUserId();
		if (userId == null) {
			throw new UnauthorizedException(HubErrorCode.UNAUTHORIZED);
		}

		UserInfo userInfo = userClient.getUserInfo(userId);
		List<Authority> allowed = Arrays.asList(preAuthorize.value());

		if (!allowed.contains(userInfo.role())) {
			log.warn("권한 거부: userId={}, role={}, allowed={}", userId, userInfo.role(), allowed);
			throw new UnauthorizedException(HubErrorCode.UNAUTHORIZED);
		}
	}
}
