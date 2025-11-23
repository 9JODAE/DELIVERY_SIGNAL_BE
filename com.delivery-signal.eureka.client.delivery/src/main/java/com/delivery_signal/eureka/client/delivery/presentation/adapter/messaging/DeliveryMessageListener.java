package com.delivery_signal.eureka.client.delivery.presentation.adapter.messaging;

import com.delivery_signal.eureka.client.delivery.application.command.CreateDeliveryCommand;
import com.delivery_signal.eureka.client.delivery.application.dto.DeliveryQueryResponse;
import com.delivery_signal.eureka.client.delivery.application.port.in.DeliveryPort;
import com.delivery_signal.eureka.client.delivery.presentation.dto.request.DeliveryCreateRequest;
import com.delivery_signal.eureka.client.delivery.presentation.mapper.DeliveryPresentationMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

/**
 * 메시지 수신 처리 (서비스 간 통신 진입점)
 * 큐에서 메시지를 받아 처리하는 리스너(Listener)
 * HTTP Controller처럼 외부 요청을 받아 Application으로 전달
 */
@Slf4j
@Service
public class DeliveryMessageListener {

    private final DeliveryPort deliveryPort;
    private final DeliveryPresentationMapper mapper;

    public DeliveryMessageListener(DeliveryPort deliveryPort, DeliveryPresentationMapper mapper) {
        this.deliveryPort = deliveryPort;
        this.mapper = mapper;
    }

    @RabbitListener(queues = "${spring.rabbitmq.exchange.queue.name}")
    public void handleDeliveryCreation(DeliveryCreateRequest request) {
        log.info("[Delivery] RabbitMQ로부터 배송 생성 요청 수신 - 주문 ID: {}", request.orderId());

        try {
            CreateDeliveryCommand command = mapper.toCreateDeliveryCommand(request);
            DeliveryQueryResponse response = deliveryPort.createDelivery(command, request.requestUserId());
            log.info("[Delivery] 배송 생성 메시지 처리 성공 - 배송 ID: {}", response.deliveryId());
        } catch (IllegalArgumentException e) {
            // 비즈니스 검증 실패 -> 재처리 불필요
            log.error("[Delivery] 배송 생성 실패 (유효성 검증) - 주문 ID: {}, {}",
                request.orderId(), e.getMessage());
            // DLQ (Dead Letter Queue)로 전달: 무한 재처리를 방지하기 위해, 특정 재시도 횟수 초과 시
            // 별도의 큐(DLQ)로 메시지를 보내 관리자가 확인하도록 함
            // DLQ 설정 위해 Spring AMQP 예외를 던짐
            // AmqpRejectAndDontRequeueException : 메시지 재시도 처리 및 Requeue 안 함 (즉시 DLQ로 보냄)
            throw new AmqpRejectAndDontRequeueException("[Delivery] 유효성 검증 실패: DLQ로 이동", e);
        } catch (Exception e) {
            log.error("[Delivery] 배송 생성 실패 (시스템 오류) - 주문 ID: {}, {}", request.orderId(), e.getMessage());
            // DB 낙관적 락 충돌 등: 잠시 후 해결될 수 있음 (일시적 오류)
            // -> 일반적인 RuntimeException은 위 yml의 retry 설정에 따라 재시도됨
            throw new RuntimeException("[Delivery] 처리 실패: DLQ로 이동", e);
        }
    }
}
