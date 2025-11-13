package com.delivery_signal.eureka.client.hub.common.filter;

import java.io.IOException;
import java.util.List;

import org.springframework.web.filter.OncePerRequestFilter;

import com.delivery_signal.eureka.client.hub.common.auth.UserContextHolder;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class UserContextFilter extends OncePerRequestFilter {
	private static final String USER_ID_HEADER = "x-user-id";
	private static final List<String> EXCLUDE_PATHS = List.of(
		"/open-api"
	);

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		String path = request.getRequestURI();
		return EXCLUDE_PATHS.stream().anyMatch(path::startsWith);
	}

	@Override
	protected void doFilterInternal(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain filterChain
	) throws ServletException, IOException {

		try {
			String userId = request.getHeader(USER_ID_HEADER);
			if (userId != null && !userId.isBlank()) {
				try {
					UserContextHolder.setUserId(userId);
				} catch (IllegalArgumentException e) {
					throw new IllegalStateException("헤더 형식이 맞지 않습니다.", e);
				}
			}
			filterChain.doFilter(request, response);
		} finally {
			UserContextHolder.clear();
		}
	}
}
