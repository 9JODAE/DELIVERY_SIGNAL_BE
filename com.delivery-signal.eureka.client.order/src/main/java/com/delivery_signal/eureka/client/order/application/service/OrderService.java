package com.delivery_signal.eureka.client.order.application.service;

import com.delivery_signal.eureka.client.order.domain.entity.Order;
import com.delivery_signal.eureka.client.order.domain.entity.OrderProduct;
import com.delivery_signal.eureka.client.order.domain.repository.OrderRepository;
import com.delivery_signal.eureka.client.order.presentation.dto.request.CreateOrderRequestDto;
import com.delivery_signal.eureka.client.order.presentation.dto.response.OrderCreateResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
public class OrderService {
    /**
     * 외부 호출인 존재할 때 비즈니스 로직
     */

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public OrderCreateResponseDto createOrder(CreateOrderRequestDto request) {

        /**
         * 원래는 외부호출이 되어야 하는 값이므로 UUID 유효성을 보장하지 못합니다.
         * 따라서 String으로 저장하고 필요할 때에 UUID로 변환해서 씁니다.
         * 그러나 현재 생성에서는 외부 의존 값을 모두 비워둘 수 없기 때문에 임시로 두 값을 임의로 입력하게 했습니다.
         * TODO 나중에는 상품명만 입력하면 공급, 수령업체 정보랑 연계되도록 해야하지 않을까요?
         */
        Order order = new Order(
                UUID.fromString(request.getSupplierCompanyId()),
                UUID.fromString(request.getReceiverCompanyId()),
                request.getRequestNote()
        );

        // OrderProduct 리스트 생성 후 order에 세팅
        List<OrderProduct> orderProducts = request.getOrderProducts().stream()
                .map(p -> OrderProduct.create(
                        order,
                        UUID.fromString(p.getProductId()),
                        p.getProductName(),
                        p.getProductPriceAtOrder(),
                        p.getQuantity()
                ))
                .toList();

        order.addOrderProducts(orderProducts); // 도메인 메서드로 추가

        //저장
        orderRepository.save(order);

        return new OrderCreateResponseDto(order.getId(), order.getCreatedBy(), order.getCreatedAt());
    }
}
