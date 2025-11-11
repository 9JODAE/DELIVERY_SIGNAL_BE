package com.delivery_signal.eureka.client.user.application.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // USER
    USER_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "U001", "사용자를 찾을 수 없습니다."),
    USER_USERNAME_DUPLICATED(HttpStatus.CONFLICT.value(), "U002", "이름은 중복될 수 없습니다"),
    USER_AUTHORIZATION_FORBIDDEN(HttpStatus.FORBIDDEN.value(), "U003", "권한이 없습니다");
    // AUTH


    // Product


    // Company


    // Message


    // Hub


    // Order


    // Delivery







    private final int status;
    private final String code;
    private final String message;

    ErrorCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }



}
