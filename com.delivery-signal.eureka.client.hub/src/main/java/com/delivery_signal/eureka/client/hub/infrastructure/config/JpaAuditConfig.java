package com.delivery_signal.eureka.client.hub.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class JpaAuditConfig {

	// @Bean TODO 추후 유저 서비스 이후 개발
	// public AuditorAware<Long> auditorProvider() {
	// 	// 현재 로그인한 사용자의 이름 반환
	//
	// }
}
