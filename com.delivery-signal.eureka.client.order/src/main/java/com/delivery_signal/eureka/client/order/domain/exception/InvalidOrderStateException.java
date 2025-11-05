package com.delivery_signal.eureka.client.order.domain.exception;

import com.delivery_signal.eureka.client.order.common.InvalidStateException;

import java.util.UUID;

public class InvalidOrderStateException extends InvalidStateException {
    public InvalidOrderStateException(String message) {
        super("[Order] " + message); // 주문 도메인에서 온 메시지임을 표시
    }
}
