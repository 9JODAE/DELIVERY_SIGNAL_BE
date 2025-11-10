package com.delivery_signal.eureka.client.user.infrastructure.config;

import com.delivery_signal.eureka.client.user.application.service.JwtUtil;
import com.delivery_signal.eureka.client.user.application.service.UserDetailsServiceImpl;
import com.delivery_signal.eureka.client.user.infrastructure.filter.JwtAuthenticationFilter;
//import com.delivery_signal.eureka.client.user.infrastructure.filter.JwtAuthorizationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity // Spring Security 지원을 가능하게 함
public class WebSecurityConfig {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final AuthenticationConfiguration authenticationConfiguration;

    public WebSecurityConfig(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService, AuthenticationConfiguration authenticationConfiguration) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.authenticationConfiguration = authenticationConfiguration;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtUtil);
        filter.setAuthenticationManager(authenticationManager(authenticationConfiguration));
        return filter;
    }

//    @Bean
//    public JwtAuthorizationFilter jwtAuthorizationFilter() {
//        return new JwtAuthorizationFilter(jwtUtil, userDetailsService);
//    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // CSRF 설정
        http.csrf((csrf) -> csrf.disable());

        // 기본 설정인 Session 방식은 사용하지 않고 JWT 방식을 사용하기 위한 설정
        http.sessionManagement((sessionManagement) ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        http.authorizeHttpRequests((authorizeHttpRequests) ->
                authorizeHttpRequests
                        .requestMatchers("/open-api/v1/auth/**").permitAll()
                        // Swagger UI 허용
                        .requestMatchers("/swagger-ui/**", "/v1/api-docs/**", "/swagger-ui.html").permitAll()
                        .anyRequest().permitAll()
        );

        http.formLogin(AbstractHttpConfigurer::disable);


        // 필터 관리
//        http.addFilterBefore(jwtAuthorizationFilter(), JwtAuthenticationFilter.class); // jwtAuthenticationFilter -> jwtAuthorizationFilter
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class); // UsernamePasswordAuthenticationFilter -> jwtAuthenticationFilter

        return http.build();
    }
}


