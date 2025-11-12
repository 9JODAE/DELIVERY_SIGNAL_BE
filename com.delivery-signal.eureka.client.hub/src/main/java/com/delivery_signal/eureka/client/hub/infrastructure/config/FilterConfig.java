package com.delivery_signal.eureka.client.hub.infrastructure.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.delivery_signal.eureka.client.hub.common.filter.UserContextFilter;

@Configuration
public class FilterConfig {

	@Bean
	public FilterRegistrationBean<UserContextFilter> userContextFilter() {
		FilterRegistrationBean<UserContextFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new UserContextFilter());
		registrationBean.setOrder(1);
		return registrationBean;
	}
}
