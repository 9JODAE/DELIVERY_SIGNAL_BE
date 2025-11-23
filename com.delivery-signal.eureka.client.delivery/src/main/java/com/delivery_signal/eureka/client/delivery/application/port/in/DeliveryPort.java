package com.delivery_signal.eureka.client.delivery.application.port.in;

import com.delivery_signal.eureka.client.delivery.application.command.CreateDeliveryCommand;
import com.delivery_signal.eureka.client.delivery.application.dto.DeliveryQueryResponse;

public interface DeliveryPort {
    DeliveryQueryResponse createDelivery(CreateDeliveryCommand command, Long requestUserId);
}
