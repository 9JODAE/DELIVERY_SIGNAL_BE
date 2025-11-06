package com.delivery_signal.eureka.client.delivery.application.service;

import com.delivery_signal.eureka.client.delivery.domain.model.Delivery;
import com.delivery_signal.eureka.client.delivery.domain.model.DeliveryStatus;
import com.delivery_signal.eureka.client.delivery.domain.repository.DeliveryRepository;
import com.delivery_signal.eureka.client.delivery.presentation.dto.request.DeliveryCreateRequest;
import com.delivery_signal.eureka.client.delivery.presentation.dto.response.DeliveryCreateResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
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

        Delivery newDelivery = deliveryRepository.save(delivery);
        return DeliveryCreateResponse.builder()
            .deliveryId(newDelivery.getDeliveryId())
            .orderId(newDelivery.getOrderId())
            .status(newDelivery.getCurrStatus().getDescription())
            .address(newDelivery.getDeliveryAddress())
            .recipient(newDelivery.getRecipient())
            .recipientSlackId(newDelivery.getRecipientSlackId())
            .deliveryManagerId(newDelivery.getDeliveryManagerId())
            .build();
    }

    // TODO: 테스트용, 추후 삭제 예정
    // DeliveryService의 헬스체크나 특정 로직에서 OrderService의 상태를 확인할 때 사용
//    public OrderServiceClient.OrderPongResponseDto checkOrderServiceStatus() {
//        try {
//            // FeignClient를 호출합니다. 'from' 파라미터에 호출자(DeliveryService) 명시
//            return orderServiceClient.ping("안녕안녕안녕");
//        } catch (Exception e) {
//            // 통신 실패 처리 (여기서 Resilience4j가 동작합니다)
//            // 실제 MSA에서는 이 통신이 실패해도 시스템이 멈추지 않도록 설계해야 합니다.
//            // ... 로그 기록 및 Fallback 처리
//            return new OrderServiceClient.OrderPongResponseDto(
//                "Order-service 통신 실패",
//                "ERROR",
//                Instant.now()
//            );
//        }
//    }
}
