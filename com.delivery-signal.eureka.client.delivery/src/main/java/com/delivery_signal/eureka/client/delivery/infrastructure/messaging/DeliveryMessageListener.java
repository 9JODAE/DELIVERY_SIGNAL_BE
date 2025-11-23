package com.delivery_signal.eureka.client.delivery.infrastructure.messaging;

import com.delivery_signal.eureka.client.delivery.application.command.CreateDeliveryCommand;
import com.delivery_signal.eureka.client.delivery.application.dto.DeliveryQueryResponse;
import com.delivery_signal.eureka.client.delivery.application.service.DeliveryService;
import com.delivery_signal.eureka.client.delivery.infrastructure.dto.DeliveryCreateDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

/**
 * 메시지 수신 처리
 * 큐에서 메시지를 받아 처리하는 리스너(Listener)
 *
 */
@Slf4j
@Service
public class DeliveryMessageListener {

    private final DeliveryService deliveryService;

    public DeliveryMessageListener(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @RabbitListener(queues = "${spring.rabbitmq.exchange.queue.name}")
    public void handleDeliveryCreation(DeliveryCreateDto request) {
        log.info("[Delivery] RabbitMQ로부터 배송 생성 요청 수신 - 주문 ID: {}", request.orderId());

        try {
            CreateDeliveryCommand command = toCreateDeliveryCommand(request);
            DeliveryQueryResponse response = deliveryService.createDelivery(command, request.requestUserId());
            log.info("[Delivery] 배송 생성 메시지 처리 성공 - 배송 ID: {}", response.deliveryId());
        } catch (Exception e) {
            log.error("[Delivery] 배송 생성 메시지 처리 중 오류 발생 - 주문 ID: {}, {}", request.orderId(), e.getMessage());
            // DLQ (Dead Letter Queue)로 전달: 무한 재처리를 방지하기 위해, 특정 재시도 횟수 초과 시
            // 별도의 큐(DLQ)로 메시지를 보내 관리자가 확인하도록 함
            // DLQ 설정 위해 Spring AMQP 예외를 던짐
            throw new AmqpRejectAndDontRequeueException("[Delivery] 처리 실패: DLQ로 이동", e);
        }
    }

    private CreateDeliveryCommand toCreateDeliveryCommand(DeliveryCreateDto request) {
        return CreateDeliveryCommand.builder()
            .userId(request.requestUserId())
            .orderId(request.orderId())
            .companyId(request.companyId())
            .status(request.status())
            .departureHubId(request.departureHubId())
            .destinationHubId(request.destinationHubId())
            .address(request.address())
            .recipient(request.recipient())
            .recipientSlackId(request.recipientSlackId())
            .build();
    }
}
