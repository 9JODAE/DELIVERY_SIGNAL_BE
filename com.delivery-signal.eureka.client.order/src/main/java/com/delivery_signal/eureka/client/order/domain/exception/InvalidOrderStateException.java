package com.delivery_signal.eureka.client.order.domain.exception;

import com.delivery_signal.eureka.client.order.common.InvalidStateException;

public class InvalidOrderStateException extends InvalidStateException {
    public InvalidOrderStateException(String message) {
        super("[Order] " + message);
    }
}
