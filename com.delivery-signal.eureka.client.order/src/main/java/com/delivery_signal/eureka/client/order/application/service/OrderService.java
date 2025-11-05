package com.delivery_signal.eureka.client.order.application.service;

import com.delivery_signal.eureka.client.order.application.command.CreateOrderCommand;
import com.delivery_signal.eureka.client.order.application.command.OrderProductCommand;
import com.delivery_signal.eureka.client.order.application.mapper.OrderQueryMapper;
import com.delivery_signal.eureka.client.order.domain.entity.Order;
import com.delivery_signal.eureka.client.order.domain.entity.OrderProduct;
import com.delivery_signal.eureka.client.order.domain.exception.OrderNotFoundException;
import com.delivery_signal.eureka.client.order.domain.repository.OrderRepository;
import com.delivery_signal.eureka.client.order.domain.service.OrderDomainService;
import com.delivery_signal.eureka.client.order.domain.vo.*;
import com.delivery_signal.eureka.client.order.infrastructure.external.company.CompanyClient;
import com.delivery_signal.eureka.client.order.infrastructure.external.hub.HubClient;
import com.delivery_signal.eureka.client.order.infrastructure.external.product.ProductClient;
import com.delivery_signal.eureka.client.order.presentation.dto.response.OrderCreateResponseDto;
import com.delivery_signal.eureka.client.order.presentation.dto.response.OrderDetailResponseDto;
import com.delivery_signal.eureka.client.order.presentation.dto.response.OrderListResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@Transactional
public class OrderService {

    private final ProductClient productClient;
    private final HubClient hubClient;
    private final CompanyClient companyClient;
    private final OrderDomainService orderDomainService;
    private final OrderRepository orderRepository;
    private final OrderQueryMapper orderQueryMapper;

    public OrderService(ProductClient productClient, HubClient hubClient, CompanyClient companyClient, OrderDomainService orderDomainService, OrderRepository orderRepository, OrderQueryMapper orderQueryMapper) {
        this.productClient = productClient;
        this.hubClient = hubClient;
        this.companyClient = companyClient;
        this.orderDomainService = orderDomainService;
        this.orderRepository = orderRepository;
        this.orderQueryMapper = orderQueryMapper;
    }

    public OrderCreateResponseDto createOrderAndSendDelivery(CreateOrderCommand command) {

        //  Command에서 UUID 리스트 추출
        List<UUID> productIds = command.getProducts().stream()
                .map(OrderProductCommand::getProductId)
                .toList();

        // 외부 서비스 호출 (Application layer 책임)
        List<ProductInfo> productInfos = productClient.getProducts(productIds);

        // 수량 매핑
        Map<UUID, Integer> quantityMap = command.getProducts().stream()
                .collect(Collectors.toMap(OrderProductCommand::getProductId, OrderProductCommand::getQuantity));

        //ProductInfo + Quantity → OrderProduct 도메인 변환
        List<OrderProduct> orderProducts = productInfos.stream()
                .map(info -> OrderProduct.builder()
                        .productId(info.getProductId())
                        .productName(info.getProductName())
                        .productPriceAtOrder(info.getPrice())
                        .transferQuantity(quantityMap.get(info.getProductId()))
                        .build())
                .toList();

//        SupplierCompanyInfo supplier = companyClient.getSupplierCompany(command.getSupplierCompanyId());
//        ReceiverCompanyInfo receiver = companyClient.getReceiverCompany(command.getReceiverCompanyId());
        UUID supplier = companyClient.getSupplierCompany(command.getSupplierCompanyId());
        UUID receiver = companyClient.getReceiverCompany(command.getReceiverCompanyId());

        UUID deliveryId = UUID.randomUUID(); // 주문 시점에 미리 배송 UUID 생성
        Order order = orderDomainService.createOrder(
                supplier, receiver, command.getRequestNote(), orderProducts, deliveryId);

        orderRepository.save(order);

        //배송 던져줄 이벤트 필요, 레디스 등.

        return new OrderCreateResponseDto(order.getId(),order.getCreatedBy(), order.getCreatedAt(), "성공");
    }

    @Transactional(readOnly = true)
    public OrderDetailResponseDto getOrderById(UUID orderId) {

        Order order = orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        return orderQueryMapper.toDetailDto(order);
    }

    @Transactional(readOnly = true)
    public List<OrderListResponseDto> getAllOrders() {

        List<Order> orders = orderRepository.findAllWithOrderProducts();
        return orderQueryMapper.toListDtos(orders);
    }
}
