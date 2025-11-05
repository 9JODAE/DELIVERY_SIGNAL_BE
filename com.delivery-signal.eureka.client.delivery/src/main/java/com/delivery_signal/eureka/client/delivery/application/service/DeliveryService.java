package com.delivery_signal.eureka.client.delivery.application.service;

import com.delivery_signal.eureka.client.delivery.domain.model.Delivery;
import com.delivery_signal.eureka.client.delivery.domain.model.DeliveryStatus;
import com.delivery_signal.eureka.client.delivery.domain.repository.DeliveryRepository;
import com.delivery_signal.eureka.client.delivery.presentation.dto.request.DeliveryCreateRequest;
import com.delivery_signal.eureka.client.delivery.presentation.dto.response.DeliveryCreateResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final OrderServiceClient orderServiceClient;

    public DeliveryService(DeliveryRepository deliveryRepository, OrderServiceClient orderServiceClient) {
        this.deliveryRepository = deliveryRepository;
        this.orderServiceClient = orderServiceClient;
    }

    /**
     * Order Service에서 Delivery Service의 API를 호출하여 내부적으로 자동 생성
     * 새로운 주문에 대한 배송 및 전체 경로 기록 생성
     */
    @Transactional
    public DeliveryCreateResponse createDelivery(Long currUserId, DeliveryCreateRequest request, String role) {
        // TODO: 배송 경로 기록은 추후에 추가 예정
        Delivery delivery = Delivery.builder()
            .orderId(request.orderId())
            .currStatus(DeliveryStatus.valueOf(request.status()))
            .departureHubId(request.departureHubId())
            .destinationHubId(request.destinationHubId())
            .deliveryAddress(request.address())
            .recipient(request.recipient())
            .recipientSlackId(request.recipientSlackId())
            .deliveryManagerId(request.deliveryManagerId())
            .build();

        Delivery saved = deliveryRepository.save(delivery);
        return DeliveryCreateResponse.from(saved);
    }
}
