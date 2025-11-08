package com.delivery_signal.eureka.client.order.application.service.internal;

import com.delivery_signal.eureka.client.order.application.port.out.DeliveryCommandPort;
import com.delivery_signal.eureka.client.order.application.dto.response.OrderForDeliveryResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InternalOrderService {

    private final DeliveryCommandPort deliveryCommandPort;

    //주문을 배송에서 조회할 수 있는 api, 필요에 따라 유지하거나 나중에 없애도 될 거 같습니다.
    @Transactional(readOnly = true)
    public OrderForDeliveryResponseDto getOrderForDelivery(UUID orderId) {
        return deliveryCommandPort.findOrderForDeliveryById(orderId)
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
