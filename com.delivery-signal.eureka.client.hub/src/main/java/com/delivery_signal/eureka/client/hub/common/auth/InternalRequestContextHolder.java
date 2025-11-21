package com.delivery_signal.eureka.client.hub.common.auth;

/**
 * 내부 통신 여부를 저장하는 ThreadLocal 컨텍스트 홀더.
 *
 * Interceptor가 "/open-api/**" 로 들어온 요청에 대해서 markInternal() 호출
 * JpaAuditConfig에서 isInternal() 결과를 보고 created_by / updated_by 값을 SYSTEM 계정으로 설정
 *
 *  내부통신 상태임을 저장하는 용도로 사용됩니다.
 * (외부 요청에서는 절대 true가 되지 않음)
 */
public class InternalRequestContextHolder {

    private static final ThreadLocal<Boolean> context = new ThreadLocal<>();

    public static void markInternal() {
        context.set(true);
    }

    public static boolean isInternal() {
        return Boolean.TRUE.equals(context.get());
    }

    public static void clear() {
        context.remove();
    }
}
