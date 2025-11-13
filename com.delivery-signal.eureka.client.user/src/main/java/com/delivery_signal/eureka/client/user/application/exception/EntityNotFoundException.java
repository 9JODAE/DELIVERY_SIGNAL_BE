package com.delivery_signal.eureka.client.user.application.exception;

public class EntityNotFoundException extends ServiceException {
    public EntityNotFoundException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public EntityNotFoundException( ) {
        super(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage());
    }

    public EntityNotFoundException(String message) {
        super(ErrorCode.USER_NOT_FOUND, message);
    }

    public EntityNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
