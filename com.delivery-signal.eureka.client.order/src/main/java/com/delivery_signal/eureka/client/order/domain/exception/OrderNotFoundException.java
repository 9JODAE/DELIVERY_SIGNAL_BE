package com.delivery_signal.eureka.client.order.domain.exception;

import com.delivery_signal.eureka.client.order.common.NotFoundException;
import java.util.UUID;

public class OrderNotFoundException extends NotFoundException {
    public OrderNotFoundException(UUID id) {
        super("[Order] 잘못된 요청입니다. :",id);
    }
}
