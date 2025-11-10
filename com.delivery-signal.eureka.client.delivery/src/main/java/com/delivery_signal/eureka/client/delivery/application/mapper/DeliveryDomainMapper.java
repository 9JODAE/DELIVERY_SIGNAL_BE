package com.delivery_signal.eureka.client.delivery.application.mapper;

import com.delivery_signal.eureka.client.delivery.application.dto.DeliveryQueryResponse;
import com.delivery_signal.eureka.client.delivery.application.dto.RouteRecordQueryResponse;
import com.delivery_signal.eureka.client.delivery.domain.model.Delivery;
import com.delivery_signal.eureka.client.delivery.domain.model.DeliveryRouteRecords;
import org.springframework.stereotype.Component;

@Component
public class DeliveryDomainMapper {

    public RouteRecordQueryResponse toResponse(DeliveryRouteRecords record) {
        if (record == null) {
            return null;
        }

        return RouteRecordQueryResponse.builder()
            .routeId(record.getRouteId())
            .deliveryId(record.getDelivery().getDeliveryId())
            .actualDistance(record.getActualDistance())
            .actualTime(record.getActualTime())
            .status(record.getCurrStatus().getDescription())
            .hubDeliveryManagerId(record.getHubDeliveryManagerId())
            .build();
    }

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
