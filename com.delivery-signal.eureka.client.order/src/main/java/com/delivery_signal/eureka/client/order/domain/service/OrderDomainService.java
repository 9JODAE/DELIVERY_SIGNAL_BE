package com.delivery_signal.eureka.client.order.domain.service;

import com.delivery_signal.eureka.client.order.domain.entity.Order;
import com.delivery_signal.eureka.client.order.domain.entity.OrderProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderDomainService {
/**
 * Todo 서비스가 나뉘는 이유
 * 도메인 내부의 순수 비즈니스 로직을 담당하는 서비스입니다.
 * 여러 Aggregate나 Entity 간의 복잡한 연산이 필요하지만,
 * 외부 시스템 호출이 없는 경우에만 사용합니다.
 *
 * 단, MSA 환경에서 복잡한 비즈니스 로직이 외부 호출(API, 이벤트 등)을 포함한다면
 * 해당 로직은 application 계층(Service)에서 처리하는 것이 적절합니다.
 */

    public Order createOrder(UUID supplier, UUID receiver, String requestNote, List<OrderProduct> orderProducts, UUID deliveryId) {


        BigDecimal totalPrice = orderProducts.stream()
                .map(p -> p.getProductPriceAtOrder().multiply(BigDecimal.valueOf(p.getTransferQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return Order.builder()
                .supplierCompanyId(supplier)
                .receiverCompanyId(receiver)
                .requestNote(requestNote)
                .totalPrice(totalPrice)
                .deliveryId(deliveryId)
                .orderProducts(orderProducts)
                .build();
    }
}
