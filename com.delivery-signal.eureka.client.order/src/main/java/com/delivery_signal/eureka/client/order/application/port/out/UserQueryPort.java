package com.delivery_signal.eureka.client.order.application.port.out;

/**
 * 유저정보를 조회하는 port
 */
public interface UserQueryPort {
    boolean isUserApproved(Long userId);
}
