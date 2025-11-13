package com.delivery_signal.eureka.client.delivery.common.exception;

import lombok.Getter;

@Getter
public class PermissionDeniedException extends RuntimeException {

    public PermissionDeniedException(String message) {
        super(message);
    }
}