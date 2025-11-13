package com.delivery_signal.eureka.client.order.application.mapper;

import com.delivery_signal.eureka.client.order.application.result.OrderDetailResult;
import com.delivery_signal.eureka.client.order.application.result.OrderListResult;
import com.delivery_signal.eureka.client.order.application.result.OrderProductResult;
import com.delivery_signal.eureka.client.order.domain.entity.Order;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 매핑용 클래스 분리
 */
@Component
public class OrderQueryMapper {

    /**
     * 상품리스트 정보를 포함한 주문단건을 매핑해서 반환하는 Dto입니다.
     * 주문리스트 내부의 상품리스트까지 N+1 문제 없이 조회하기 위해 사용합니다.
     *
     * @param order 매핑할 order 값
     * @return 내부에 상품 List가 포함된 단일 order값
     */
    public OrderListResult toListDto(Order order) {
        return OrderListResult.builder()
                .orderId(order.getId())
                .supplierCompanyId(order.getSupplierCompanyId())
                .receiverCompanyId(order.getReceiverCompanyId())
                .deliveryId(order.getDeliveryId())
                .products(order.getOrderProducts().stream()
                        .map(OrderProductResult::from)
                        .toList())
                .totalPrice(order.getTotalPriceAtOrder())
                .requestNote(order.getRequestNote())
                .createdAt(order.getCreatedAt())
                .createdBy(order.getCreatedBy())
                .updatedAt(order.getUpdatedAt())
                .updatedBy(order.getUpdatedBy())
                .deletedAt(order.getDeletedAt())
                .deletedBy(order.getDeletedBy())
                .build();
    }

    /**
     * 전체 주문 리스트를 매핑하는 메서드입니다.
     *
     * @param orders Dto타입으로 매핑해야하는 주문들
     * @return 매핑된 주문들
     */
    public List<OrderListResult> toListDtos(List<Order> orders) {
        return orders.stream()
                .map(this::toListDto)
                .toList();
    }

    /**
     * 주문 상세 단일 조회 시 사용하는 메서드 입니다.
     *
     * @param order 상세조회할 주문 값
     * @return 매핑된 주문의 단일 정보(상품정보 리스트 포함)
     */
    public OrderDetailResult toDetailDto(Order order) {
        return OrderDetailResult.builder()
                .orderId(order.getId())
                .supplierCompanyId(order.getSupplierCompanyId())
                .receiverCompanyId(order.getReceiverCompanyId())
                .products(order.getOrderProducts().stream()
                        .map(OrderProductResult::from)
                        .toList())
                .totalPrice(order.getTotalPriceAtOrder())
                .requestNote(order.getRequestNote())
                .createdAt(order.getCreatedAt())
                .createdBy(order.getCreatedBy())
                .updatedAt(order.getUpdatedAt())
                .updatedBy(order.getUpdatedBy())
                .build();
    }
}
