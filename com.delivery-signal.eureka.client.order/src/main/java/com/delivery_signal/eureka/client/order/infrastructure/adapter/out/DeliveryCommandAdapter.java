package com.delivery_signal.eureka.client.order.infrastructure.adapter.out;

import com.delivery_signal.eureka.client.order.application.command.CreateDeliveryCommand;
import com.delivery_signal.eureka.client.order.application.port.out.DeliveryCommandPort;
import com.delivery_signal.eureka.client.order.domain.vo.delivery.DeliveryCreatedInfo;
import com.delivery_signal.eureka.client.order.infrastructure.client.delivery.DeliveryClient;
import com.delivery_signal.eureka.client.order.infrastructure.client.delivery.dto.DeliveryCreateRequestDto;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * 배송 관련 커맨드 어댑터
 * - 배송 생성/취소 호출 담당
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DeliveryCommandAdapter implements DeliveryCommandPort {

    private final DeliveryClient deliveryClient;

    @Override
    public DeliveryCreatedInfo createDelivery(CreateDeliveryCommand command) {

        DeliveryCreateRequestDto requestDto = DeliveryCreateRequestDto.builder()
                .orderId(command.getOrderId())
                .companyId(command.getSupplierCompanyId())
                .departureHubId(command.getDepartureHubId())
                .destinationHubId(command.getDestinationHubId())
                .recipient(command.getRecipient())
                .address(command.getAddress())
                .recipientSlackId(command.getRecipientSlackId())
                .status("허브 대기중")
                .build();

        return deliveryClient.createDelivery(requestDto,command.getUserId(),command.getUserRole()).getData();
    }


    @Override
    public void cancelDelivery(UUID deliveryId) {
        try {
            Long systemUserId = 0L;
            String systemRole = "MASTER";
            deliveryClient.cancelDelivery(deliveryId, systemUserId, systemRole);
            log.info("배송 취소 요청 완료: {}", deliveryId);
        } catch (FeignException.NotFound e) {
            log.warn("배송이 존재하지 않거나 이미 취소됨: {}", deliveryId);
        } catch (FeignException e) {
            log.error("배송 취소 요청 실패: {}", e.contentUTF8(), e);
            throw new RuntimeException("배송 취소 요청 실패", e);
        }
    }
}