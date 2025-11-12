package com.delivery_signal.eureka.client.hub.infrastructure.config;

import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.delivery_signal.eureka.client.hub.common.auth.UserContextHolder;
import com.delivery_signal.eureka.client.hub.common.error.HubErrorCode;
import com.delivery_signal.eureka.client.hub.common.exception.UnauthorizedException;

@Configuration
@EnableJpaAuditing
public class JpaAuditConfig {

	@Bean
	public AuditorAware<Long> auditorProvider() {
		return () -> {
			String userId = UserContextHolder.getUserId();
			if (userId == null || userId.isBlank()) {
				throw new UnauthorizedException(HubErrorCode.UNAUTHORIZED);
			}
			return Optional.of(Long.parseLong(userId));
		};
	}
}
