package com.delivery_signal.eureka.client.user.application.exception;

public class UnauthorizedAccessException extends ServiceException{
    public UnauthorizedAccessException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public UnauthorizedAccessException(String userRole) {
        super(ErrorCode.USER_AUTHORIZATION_FORBIDDEN, userRole + " 권한으로만 접근이 가능합니다");
    }

    public UnauthorizedAccessException(ErrorCode errorCode) {
        super(errorCode);
    }
}
