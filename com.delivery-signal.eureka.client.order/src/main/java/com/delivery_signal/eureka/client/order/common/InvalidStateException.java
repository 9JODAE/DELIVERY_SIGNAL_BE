package com.delivery_signal.eureka.client.order.common;


public class InvalidStateException extends RuntimeException {
    public InvalidStateException(String message) {
        super(message);
    }
}

