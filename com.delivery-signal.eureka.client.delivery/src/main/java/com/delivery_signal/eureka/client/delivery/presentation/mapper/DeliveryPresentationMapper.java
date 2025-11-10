package com.delivery_signal.eureka.client.delivery.presentation.mapper;

import com.delivery_signal.eureka.client.delivery.application.command.CreateDeliveryCommand;
import com.delivery_signal.eureka.client.delivery.application.command.RouteSegmentCommand;
import com.delivery_signal.eureka.client.delivery.presentation.dto.request.DeliveryCreateRequest;
import org.springframework.stereotype.Component;

@Component
public class DeliveryPresentationMapper {
    /**
     * Request DTO -> Application Command
     */
    public CreateDeliveryCommand toCreateDeliveryCommand(DeliveryCreateRequest request) {
        return CreateDeliveryCommand.builder()
            .orderId(request.orderId())
            .companyId(request.companyId())
            .status(request.status())
            .departureHubId(request.departureHubId())
            .destinationHubId(request.destinationHubId())
            .routes(request.routes() != null ?
                request.routes().stream().map(this::toRouteSegmentCommand).toList() :
                null)
            .address(request.address())
            .recipient(request.recipient())
            .recipientSlackId(request.recipientSlackId())
            .deliveryManagerId(request.deliveryManagerId())
            .build();

    }

    private RouteSegmentCommand toRouteSegmentCommand(DeliveryCreateRequest.RouteSegmentDto dto) {
        return RouteSegmentCommand.builder()
            .sequence(dto.sequence())
            .departureHubId(dto.departureHubId())
            .destinationHubId(dto.destinationHubId())
            .estDistance(dto.estDistance())
            .estTime(dto.estTime())
            .build();
    }
}
