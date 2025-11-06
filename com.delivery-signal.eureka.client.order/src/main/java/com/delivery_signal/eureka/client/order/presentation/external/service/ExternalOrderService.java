package com.delivery_signal.eureka.client.order.presentation.external.service;

import com.delivery_signal.eureka.client.order.application.port.out.ExternalOrderQueryPort;
import com.delivery_signal.eureka.client.order.presentation.external.dto.response.OrderForDeliveryResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ExternalOrderService {

    private final ExternalOrderQueryPort externalOrderQueryPort;

    @Transactional(readOnly = true)
    public OrderForDeliveryResponseDto getOrderForDelivery(UUID orderId) {
        return externalOrderQueryPort.findOrderForDeliveryById(orderId)
                .map(dto -> OrderForDeliveryResponseDto.builder()
                        .orderId(dto.getOrderId())
                        .supplierCompanyId(dto.getSupplierCompanyId())
                        .receiverCompanyId(dto.getReceiverCompanyId())
                        .deliveryId(dto.getDeliveryId())
                        .requestNote(dto.getRequestNote())
                        .build()
                )
                .orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다: " + orderId));
    }

}
