package com.delivery_signal.eureka.client.order.presentation.external.mapper.response;

import com.delivery_signal.eureka.client.order.application.result.*;
import com.delivery_signal.eureka.client.order.presentation.external.dto.response.*;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

public class OrderResponseMapper {

    public static OrderCreateResponseDto toCreateResponse(OrderCreateResult result) {
        return OrderCreateResponseDto.builder()
                .orderId(result.getOrderId())
                .createBy(result.getCreateBy())
                .createAt(result.getCreateAt())
                .message("주문이 생성되었습니다.")
                .build();
    }

    public static OrderDetailResponseDto toDetailResponse(OrderDetailResult result) {
        return OrderDetailResponseDto.builder()
                .orderId(result.getOrderId())
                .supplierCompanyId(result.getSupplierCompanyId())
                .receiverCompanyId(result.getReceiverCompanyId())
                .deliveryId(result.getDeliveryId())
                .products(result.getProducts().stream()
                        .map(OrderProductResponseMapper::toResponseDto)
                        .collect(Collectors.toList()))
                .totalPrice(result.getTotalPrice())
                .requestNote(result.getRequestNote())
                .createdAt(result.getCreatedAt())
                .createdBy(result.getCreatedBy())
                .updatedAt(result.getUpdatedAt())
                .updatedBy(result.getUpdatedBy())
                .build();
    }


    // 리스트 조회용
    public static OrderListResponseDto toListResponse(OrderListResult result) {
        return OrderListResponseDto.builder()
                .orderId(result.getOrderId())
                .supplierCompanyId(result.getSupplierCompanyId())
                .receiverCompanyId(result.getReceiverCompanyId())
                .deliveryId(result.getDeliveryId())
                .products(result.getProducts().stream()
                        .map(OrderProductResponseMapper::toResponseDto)
                        .collect(Collectors.toList()))
                .totalPrice(result.getTotalPrice())
                .requestNote(result.getRequestNote())
                .createdAt(result.getCreatedAt())
                .createdBy(result.getCreatedBy())
                .updatedAt(result.getUpdatedAt())
                .updatedBy(result.getUpdatedBy())
                .deletedAt(result.getDeletedAt())
                .deletedBy(result.getDeletedBy())
                .build();
    }

    public static OrderUpdateResponseDto toUpdateResponse(OrderUpdateResult result) {
        return OrderUpdateResponseDto.builder()
                .orderId(result.getOrderId())
                .updatedBy(result.getUpdatedBy())
                .updatedAt(result.getUpdatedAt() != null ? result.getUpdatedAt() : LocalDateTime.now())
                .message("주문 일부 정보가 수정되었습니다.")
                .build();
    }

    public static OrderCancelResponseDto toCancelResponse(OrderCancelResult result) {
        return OrderCancelResponseDto.builder()
                .orderId(result.getOrderId())
                .deliveryId(result.getDeliveryId())
                .orderStatus(result.getOrderStatus())
                .message(result.getMessage())
                .build();
    }

    public static OrderDeleteResponseDto toDeleteResponse(OrderDeleteResult result) {
        return OrderDeleteResponseDto.builder()
                .orderId(result.getOrderId())
                .deletedBy(result.getDeletedBy())
                .deletedAt(result.getDeletedAt() != null ? result.getDeletedAt() : LocalDateTime.now())
                .message("주문이 삭제되었습니다.")
                .build();
    }
}
