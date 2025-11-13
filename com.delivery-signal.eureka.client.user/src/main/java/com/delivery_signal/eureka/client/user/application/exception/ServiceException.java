package com.delivery_signal.eureka.client.user.application.exception;

import lombok.Getter;

@Getter
public class ServiceException extends RuntimeException {

    private ErrorCode errorCode;

    public ServiceException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ServiceException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    @Override
    public String toString() {
        return "ServiceException{" +
                "errorCode=" + errorCode +
                '}';
    }
}