package com.delivery_signal.eureka.client.hub.infrastructure.config;

import com.delivery_signal.eureka.client.hub.common.interceptor.InternalCallInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final InternalCallInterceptor internalCallInterceptor;

    public WebConfig(InternalCallInterceptor internalCallInterceptor) {
        this.internalCallInterceptor = internalCallInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(internalCallInterceptor)
                .addPathPatterns("/open-api/**");
    }
}