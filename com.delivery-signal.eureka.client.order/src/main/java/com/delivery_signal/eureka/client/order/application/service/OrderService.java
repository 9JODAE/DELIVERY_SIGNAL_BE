package com.delivery_signal.eureka.client.order.application.service;

import com.delivery_signal.eureka.client.order.application.command.*;
import com.delivery_signal.eureka.client.order.application.dto.response.*;
import com.delivery_signal.eureka.client.order.application.mapper.OrderQueryMapper;
import com.delivery_signal.eureka.client.order.application.service.external.*;
import com.delivery_signal.eureka.client.order.domain.entity.*;
import com.delivery_signal.eureka.client.order.domain.exception.OrderNotFoundException;
import com.delivery_signal.eureka.client.order.domain.repository.*;
import com.delivery_signal.eureka.client.order.domain.service.OrderDomainService;
import com.delivery_signal.eureka.client.order.domain.vo.ProductInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {

    private final ProductService productService;
    private final CompanyService companyService;
    private final HubService hubService;

    private final OrderDomainService orderDomainService;
    private final OrderQueryMapper orderQueryMapper;
    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;

    public OrderService(ProductService productService,
                        CompanyService companyService,
                        HubService hubService,
                        OrderDomainService orderDomainService,
                        OrderQueryMapper orderQueryMapper,
                        OrderRepository orderRepository,
                        OrderProductRepository orderProductRepository) {
        this.productService = productService;
        this.companyService = companyService;
        this.hubService = hubService;
        this.orderDomainService = orderDomainService;
        this.orderQueryMapper = orderQueryMapper;
        this.orderRepository = orderRepository;
        this.orderProductRepository = orderProductRepository;
    }

    public OrderCreateResponseDto createOrderAndSendDelivery(CreateOrderCommand command) {
        List<UUID> productIds = command.getProducts().stream()
                .map(OrderProductCommand::getProductId)
                .toList();

        List<ProductInfo> productInfos = productService.getProducts(productIds);

        Map<UUID, Integer> quantityMap = command.getProducts().stream()
                .collect(Collectors.toMap(OrderProductCommand::getProductId, OrderProductCommand::getQuantity));

        List<OrderProduct> orderProducts = productInfos.stream()
                .map(info -> OrderProduct.builder()
                        .productId(info.getProductId())
                        .productName(info.getProductName())
                        .productPriceAtOrder(info.getPrice())
                        .transferQuantity(quantityMap.get(info.getProductId()))
                        .build())
                .toList();

        UUID supplier = companyService.getSupplierCompany(command.getSupplierCompanyId());
        UUID receiver = companyService.getReceiverCompany(command.getReceiverCompanyId());

        UUID deliveryId = UUID.randomUUID();
        Order order = orderDomainService.createOrder(
                supplier, receiver, command.getRequestNote(), orderProducts, deliveryId);

        orderRepository.save(order);

        return new OrderCreateResponseDto(order.getId(), order.getCreatedBy(), order.getCreatedAt(), "성공");
    }

    @Transactional(readOnly = true)
    public OrderDetailResponseDto getOrderById(UUID orderId) {
        Order order = orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
        return orderQueryMapper.toDetailDto(order); // Application Layer 내 Mapper 사용
    }

    @Transactional(readOnly = true)
    public List<OrderListResponseDto> getAllOrders() {
        List<Order> orders = orderRepository.findAllWithOrderProducts();
        return orderQueryMapper.toListDtos(orders);
    }

    public OrderUpdateResponseDto updateOrder(UUID orderId, UpdateOrderCommand command) {
        Order order = orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        order.updateRequestNote(command.getRequestNote());

        order.getOrderProducts().stream()
                .filter(op -> op.getProductId().equals(command.getProductId()))
                .findFirst()
                .orElseThrow(() -> new OrderNotFoundException(command.getProductId()))
                .updateQuantity(command.getTransferQuantity());

        return OrderUpdateResponseDto.toResponse(order.getId(), order.getUpdatedBy());
    }

    public OrderDeleteResponseDto deleteOrder(DeleteOrderCommand command) {
        Order order = orderRepository.findByOrderId(command.getOrderId())
                .orElseThrow(() -> new OrderNotFoundException(command.getOrderId()));

        List<OrderProduct> orderProducts = orderProductRepository.findAllByOrderId(order.getId());
        LocalDateTime now = LocalDateTime.now();

        orderProducts.forEach(p -> p.markAsDeleted(now));
        order.markAsDeleted(now);

        orderRepository.save(order);
        orderProductRepository.saveAll(orderProducts);

        return OrderDeleteResponseDto.toResponse(order.getId(), order.getDeletedBy(), now);
    }
}
