package com.delivery_signal.eureka.client.delivery.presentation.mapper;

import com.delivery_signal.eureka.client.delivery.application.command.CreateDeliveryCommand;
import com.delivery_signal.eureka.client.delivery.application.command.RouteSegmentCommand;
import com.delivery_signal.eureka.client.delivery.application.command.UpdateDeliveryInfoCommand;
import com.delivery_signal.eureka.client.delivery.application.command.UpdateDeliveryStatusCommand;
import com.delivery_signal.eureka.client.delivery.application.command.UpdateRouteRecordCommand;
import com.delivery_signal.eureka.client.delivery.presentation.dto.request.DeliveryCreateRequest;
import com.delivery_signal.eureka.client.delivery.presentation.dto.request.DeliveryInfoUpdateRequest;
import com.delivery_signal.eureka.client.delivery.presentation.dto.request.DeliveryStatusUpdateRequest;
import com.delivery_signal.eureka.client.delivery.presentation.dto.request.RouteRecordUpdateRequest;
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

    /**
     * Request DTO -> Application Command
     */
    public UpdateDeliveryStatusCommand toUpdateDeliveryStatusCommand(DeliveryStatusUpdateRequest request) {
        return UpdateDeliveryStatusCommand.builder()
            .newStatus(request.newStatus())
            .build();
    }

    public UpdateDeliveryInfoCommand toUpdateDeliveryInfoCommand(DeliveryInfoUpdateRequest request) {
        return UpdateDeliveryInfoCommand.builder()
            .address(request.address())
            .recipient(request.recipient())
            .recipientSlackId(request.recipientSlackId())
            .build();
    }

    public UpdateRouteRecordCommand toUpdateRouteRecordCommand(RouteRecordUpdateRequest request) {
        return UpdateRouteRecordCommand.builder()
            .newStatus(request.newStatus())
            .actualDistance(request.actualDistance())
            .actualTime(request.actualTime())
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
