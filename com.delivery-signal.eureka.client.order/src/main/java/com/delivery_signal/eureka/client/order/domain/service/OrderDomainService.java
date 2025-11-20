package com.delivery_signal.eureka.client.order.domain.service;

import com.delivery_signal.eureka.client.order.domain.entity.Order;
import com.delivery_signal.eureka.client.order.domain.entity.OrderProduct;
import com.delivery_signal.eureka.client.order.domain.repository.OrderRepository;
import com.delivery_signal.eureka.client.order.domain.vo.product.ProductInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderDomainService {

    private final OrderRepository orderRepository;


    /**
     * 도메인 규칙에 따라 Order + OrderProduct를 모두 조립하는 팩토리 역할
     */
    public Order createOrder(
            UUID supplierCompanyId,
            UUID receiverCompanyId,
            UUID departureHubId,
            UUID arrivalHubId,
            String requestNote,
            List<ProductInfo> productInfos,
            Map<UUID, Integer> productQuantities
    ) {
        // 1. 총 금액 계산
        BigDecimal totalPrice = productInfos.stream()
                .map(info -> {
                    Integer qty = productQuantities.get(info.getProductId());
                    if (qty == null || qty <= 0) {
                        throw new IllegalArgumentException("수량 정보가 유효하지 않습니다. productId=" + info.getProductId());
                    }
                    return info.getPrice()
                            .multiply(BigDecimal.valueOf(qty));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 2. Order 생성
        Order order = Order.of(
                supplierCompanyId,
                receiverCompanyId,
                departureHubId,
                arrivalHubId,
                requestNote,
                totalPrice,
                null //주문 생성 시 배송id는 null처리
        );

        // 3. OrderProduct 생성 + 연관관계 설정
        for (ProductInfo info : productInfos) {
            Integer qty = productQuantities.get(info.getProductId());
            if (qty == null || qty <= 0) {
                throw new IllegalArgumentException("수량 정보가 유효하지 않습니다. productId=" + info.getProductId());
            }

            OrderProduct op = OrderProduct.create(
                    order,
                    info.getProductId(),
                    info.getProductName(),
                    info.getPrice(),
                    qty
            );

            order.getOrderProducts().add(op);
        }

        return order;
    }


    // OrderDomainService
    public List<Order> getOrdersByHub(UUID hubId) {
        return orderRepository.findAllWithOrderProducts().stream()
                .filter(order -> hubId.equals(order.getDepartureHubId())) // 출발 허브 기준
                .filter(order -> order.getDeletedAt() == null)       // 논리 삭제 제외
                .toList();
    }


}
