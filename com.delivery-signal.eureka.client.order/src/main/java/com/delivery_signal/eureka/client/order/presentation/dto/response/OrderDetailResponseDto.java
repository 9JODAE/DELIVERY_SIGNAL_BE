package com.delivery_signal.eureka.client.order.presentation.dto.response;


import com.delivery_signal.eureka.client.order.application.dto.response.OrderQueryResponseDto;
import com.delivery_signal.eureka.client.order.domain.entity.Order;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
public class OrderDetailResponseDto {
    UUID orderId;
    UUID supplierCompanyId;
    UUID receiverCompanyId;
    UUID deliveryId;
    List<OrderQueryResponseDto> products;
    BigDecimal totalPrice;
    String requestNote;
    LocalDateTime createdAt;
    UUID createdBy;
    LocalDateTime updatedAt;
    UUID updatedBy;

    public OrderDetailResponseDto(
            UUID id,
            UUID supplierCompanyId,
            UUID receiverCompanyId,
            List<OrderQueryResponseDto> products,
            UUID deliveryId,
            BigDecimal totalPriceAtOrder,
            String requestNote) {

        this.orderId = id;
        this.supplierCompanyId = supplierCompanyId;
        this.receiverCompanyId = receiverCompanyId;
        this.products = products;
        this.deliveryId = deliveryId;
        this.totalPrice = totalPriceAtOrder;
        this.requestNote = requestNote;
    }

    public static OrderDetailResponseDto from(Order order) {
        return new OrderDetailResponseDto(
                order.getId(),
                order.getSupplierCompanyId(),
                order.getReceiverCompanyId(),
                order.getOrderProducts().stream()
                        .map(OrderQueryResponseDto::from) // 리스트 내부 매핑
                        .toList(),
                order.getDeliveryId(),
                order.getTotalPriceAtOrder(),
                order.getRequestNote()
        );

    }
}
