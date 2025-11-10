package com.delivery_signal.eureka.client.delivery.application.mapper;

import com.delivery_signal.eureka.client.delivery.application.dto.DeliveryQueryResponse;
import com.delivery_signal.eureka.client.delivery.domain.model.Delivery;
import org.springframework.stereotype.Component;

@Component
public class DeliveryDomainMapper {
    public DeliveryQueryResponse toResponse(Delivery delivery) {
        if (delivery == null) {
            return null;
        }

        return DeliveryQueryResponse.builder()
            .deliveryId(delivery.getDeliveryId())
            .orderId(delivery.getOrderId())
            .departureHubId(delivery.getDepartureHubId())
            .destinationHubId(delivery.getDestinationHubId())
            .status(String.valueOf(delivery.getCurrStatus()))
            .address(delivery.getDeliveryAddress())
            .recipient(delivery.getRecipient())
            .recipientSlackId(delivery.getRecipientSlackId())
            .deliveryManagerId(delivery.getDeliveryManagerId())
            .build();
    }
}
