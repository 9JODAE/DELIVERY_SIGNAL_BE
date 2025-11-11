package com.delivery_signal.eureka.client.order.common;

import java.util.UUID;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String entityName, UUID id) {
        super(entityName + "이(가) 존재하지 않습니다. id=" + id);
    }

    public NotFoundException(Long userId) {
        super("해당 사용자는 존재하지 않습니다. id=" + userId);
    }
}