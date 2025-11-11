package com.delivery_signal.eureka.client.user.common.auth.jwt;

import com.delivery_signal.eureka.client.user.common.auth.util.JwtUtil;
import com.delivery_signal.eureka.client.user.common.auth.security.UserDetailsImpl;
import com.delivery_signal.eureka.client.user.domain.entity.ApprovalStatus;
import com.delivery_signal.eureka.client.user.domain.entity.UserRole;
import com.delivery_signal.eureka.client.user.presentation.dto.request.CreateUserLoginRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Slf4j(topic = "로그인 및 JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
        setFilterProcessesUrl("/open-api/v1/auth/token");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("로그인 시도");
        try {
            CreateUserLoginRequest requestDto = new ObjectMapper().readValue(request.getInputStream(), CreateUserLoginRequest.class);

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestDto.username(),
                            requestDto.password(),
                            null
                    )
            );
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        Long userId = ((UserDetailsImpl) authResult.getPrincipal()).getUser().getUserId();
        String username = ((UserDetailsImpl) authResult.getPrincipal()).getUsername();
        UserRole role = ((UserDetailsImpl) authResult.getPrincipal()).getUser().getRole();
        ApprovalStatus status = ((UserDetailsImpl) authResult.getPrincipal()).getUser().getApprovalStatus();

        // approvalStatus 체크
        if (ApprovalStatus.PENDING.equals(status)) {
            log.warn("로그인 거부: userId={}, approvalStatus!=APPROVED", userId);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("사용자 승인 대기 상태입니다.");
            response.getWriter().flush();
            return;
        }
        if (ApprovalStatus.REJECTED.equals(status)) {
            log.warn("로그인 거부: userId={}, approvalStatus!=APPROVED", userId);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("사용자 승인이 거절되었습니다.");
            response.getWriter().flush();
            return;
        }

        log.info("로그인 성공 및 JWT 생성");
        String token = jwtUtil.createAccessToken(userId, username, role);
        jwtUtil.addAccessTokenToHeader(token, response);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        log.info("로그인 실패");
        response.setStatus(401);
    }
}