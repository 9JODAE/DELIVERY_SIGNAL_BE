package com.delivery_signal.eureka.client.delivery.common.auth;

public class UserContextHolder {
    private static final ThreadLocal<String> context = new ThreadLocal<>();

    public static void setUserId(String userId) {
        context.set(userId);
    }

    public static String getUserId() {
        return context.get();
    }

    public static void clear() {
        context.remove();
    }
}
