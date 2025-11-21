package com.delivery_signal.eureka.client.hub.common.interceptor;

import com.delivery_signal.eureka.client.hub.common.auth.InternalRequestContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 내부 통신(open-api) 요청을 식별하기 위한 인터셉터.
 *
 * - "/open-api/**" 로 들어오는 요청은 모두 내부 서비스 간 통신
 * - 내부 통신은 사용자 인증(JWT, x-user-id)이 없기 때문에 UserContextHolder에 값이 없음.
 * - 하지만 JPA Auditing(created_by / updated_by)에서는 매 요청마다 "누가 작업했는지" 필요하기 때문에
 * - 내부 통신일 경우 "SYSTEM" 계정을 추가하도록 했습니다.
 *
 * 이 인터셉터는 요청 URI가 "/open-api" 로 시작할 경우
 * InternalRequestContextHolder.markInternal() 을 호출해
 * "이 요청은 내부 통신이다" 라는 플래그를 ThreadLocal에 저장합니다.
 *
 * 요청이 끝난 후(afterCompletion)에는 ThreadLocal을 반드시 clear() 해야
 * 다른 요청에 값이 섞이지 않습니다. (ThreadLocal 메모리 누수 / 오염 방지).
 */
@Component
public class InternalCallInterceptor implements HandlerInterceptor {

    private static final String INTERNAL_PREFIX = "/open-api";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String uri = request.getRequestURI();

        // 내부 통신은 모두 /open-api/** 규칙을 따름
        if (uri.startsWith(INTERNAL_PREFIX)) {
            InternalRequestContextHolder.markInternal();
        }

        return true;
    }

    @Override
    public void afterCompletion(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            Exception ex
    ) {
        // ThreadLocal 오염 방지
        InternalRequestContextHolder.clear();
    }
}
