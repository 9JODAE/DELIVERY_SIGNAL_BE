package com.delivery_signal.eureka.client.order.application.service;

import com.delivery_signal.eureka.client.order.application.command.CreateOrderCommand;
import com.delivery_signal.eureka.client.order.application.command.DeleteOrderCommand;
import com.delivery_signal.eureka.client.order.application.command.OrderProductCommand;
import com.delivery_signal.eureka.client.order.application.command.UpdateOrderCommand;
import com.delivery_signal.eureka.client.order.application.command.CreateDeliveryCommand;
import com.delivery_signal.eureka.client.order.application.mapper.OrderQueryMapper;
import com.delivery_signal.eureka.client.order.application.port.out.*;
import com.delivery_signal.eureka.client.order.application.result.*;
import com.delivery_signal.eureka.client.order.application.validator.OrderPermissionValidator;
import com.delivery_signal.eureka.client.order.common.NotFoundException;
import com.delivery_signal.eureka.client.order.domain.entity.Order;
import com.delivery_signal.eureka.client.order.domain.entity.OrderProduct;
import com.delivery_signal.eureka.client.order.domain.exception.OrderNotFoundException;
import com.delivery_signal.eureka.client.order.domain.repository.OrderProductRepository;
import com.delivery_signal.eureka.client.order.domain.repository.OrderRepository;
import com.delivery_signal.eureka.client.order.domain.service.OrderDomainService;
import com.delivery_signal.eureka.client.order.domain.vo.company.CompanyInfo;
import com.delivery_signal.eureka.client.order.domain.vo.delivery.DeliveryCreatedInfo;
import com.delivery_signal.eureka.client.order.domain.vo.product.ProductInfo;
import com.delivery_signal.eureka.client.order.domain.vo.user.UserAuthorizationInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@Transactional
public class OrderService {

    //요청으로 인해 외부 msa의 쓰기사용
    private final DeliveryCommandPort deliveryCommandPort;
    private final HubCommandPort hubCommandPort;

    //요청으로 인해 외부 msa의 읽기사용
    private final HubQueryPort hubQueryPort;
    private final CompanyQueryPort companyQueryPort;
    private final ProductQueryPort productQueryPort;
    private final UserQueryPort userQueryPort;

    private final OrderDomainService orderDomainService;
    private final OrderQueryMapper orderQueryMapper;
    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
    private final OrderPermissionValidator orderPermissionValidator;

    public OrderService(DeliveryCommandPort deliveryCommandPort, HubCommandPort hubCommandPort, HubQueryPort hubQueryPort, CompanyQueryPort companyQueryPort, ProductQueryPort productQueryPort, UserQueryPort userQueryPort, OrderDomainService orderDomainService, OrderQueryMapper orderQueryMapper, OrderRepository orderRepository, OrderProductRepository orderProductRepository, OrderPermissionValidator orderPermissionValidator) {
        this.deliveryCommandPort = deliveryCommandPort;
        this.hubCommandPort = hubCommandPort;
        this.hubQueryPort = hubQueryPort;
        this.companyQueryPort = companyQueryPort;
        this.productQueryPort = productQueryPort;
        this.userQueryPort = userQueryPort;
        this.orderDomainService = orderDomainService;
        this.orderQueryMapper = orderQueryMapper;
        this.orderRepository = orderRepository;
        this.orderProductRepository = orderProductRepository;
        this.orderPermissionValidator = orderPermissionValidator;
    }


    /**
     * 주문 생성 -> 배송생성요청
     *
     * @param command 입력된 주문 정보
     * @return 주문 결과
     */
    public OrderCreateResult createOrderAndSendDelivery(CreateOrderCommand command) {

        // 로그인 및 최소 권한 체크
        orderPermissionValidator.validateCreate(command.getUserId());

        //유저 활성 여부 확인
        if (!userQueryPort.isUserApproved(command.getUserId())) {
            throw new NotFoundException(command.getUserId());
        }

        // 상품 ID 리스트
        List<UUID> productIds = command.getProducts().stream()
                .map(OrderProductCommand::getProductId)
                .toList();

        // 상품 정보 조회
        List<ProductInfo> productInfos = productQueryPort.getProducts(productIds);

        // 상품 검증: 각 상품이 공급업체에 속하는지 확인
        productInfos.stream()
                .filter(p -> !p.getCompanyId().equals(command.getSupplierCompanyId()))
                .findFirst()
                .ifPresent(p -> {
                    throw new NotFoundException("상품", p.getProductId());
                });

        // 업체 존재 여부 검증
        CompanyInfo supplier = companyQueryPort.getCompanyById(command.getSupplierCompanyId());
        CompanyInfo receiver = companyQueryPort.getCompanyById(command.getReceiverCompanyId());

        // 허브 재고 조회
        Map<UUID, Integer> productStocks = hubQueryPort.getStockQuantities(productIds);

        // 재고 부족 시 빠른 실패
        command.getProducts().stream()
                .filter(p -> productStocks.getOrDefault(p.getProductId(), 0) < p.getQuantity())
                .findFirst()
                .ifPresent(p -> {
                    throw new IllegalStateException("재고 부족: 상품 " + p.getProductId() + "의 재고가 부족합니다.");
                });

        // 주문 상품 도메인 생성
        UUID deliveryId = UUID.randomUUID();
        List<OrderProduct> orderProducts = productInfos.stream()
                .map(info -> {
                    Integer quantity = command.getProducts().stream()
                            .filter(p -> p.getProductId().equals(info.getProductId()))
                            .findFirst()
                            .map(OrderProductCommand::getQuantity)
                            .orElse(0);
                    return OrderProduct.builder()
                            .productId(info.getProductId())
                            .productName(info.getProductName())
                            .productPriceAtOrder(info.getPrice())
                            .transferQuantity(quantity)
                            .build();
                })
                .toList();

        // 주문 생성 (도메인 로직)
        Order order = orderDomainService.createOrder(
                supplier.getCompanyId(),    // 공급업체 ID
                receiver.getCompanyId(),    // 수령업체 ID
                supplier.getHubId(),        // 출발 허브 ID
                receiver.getHubId(),        // 도착 허브 ID
                command.getRequestNote(),
                orderProducts,
                deliveryId
        );

        // 주문 저장
        orderRepository.save(order);

        // 허브 재고 차감 요청
        hubCommandPort.deductStocks(order.getDepartureHubId(), command.getProducts());

        // 배송 생성 요청
        CreateDeliveryCommand deliveryRequest = CreateDeliveryCommand.builder()
                .deliveryId(deliveryId)
                .orderId(order.getId())
                .supplierCompanyId(command.getSupplierCompanyId())
                .receiverCompanyId(command.getReceiverCompanyId())
                .fromHubId(supplier.getHubId())
                .toHubId(receiver.getHubId())
                .address(receiver.getAddress())
                .build();

        DeliveryCreatedInfo deliveryInfo = deliveryCommandPort.createDelivery(deliveryRequest);

        log.info("주문 id: {}, 배송 요청 완료: {}", deliveryRequest.getOrderId(), deliveryInfo.getMessage());

        return new OrderCreateResult(
                order.getId(),
                order.getCreatedBy(),
                order.getCreatedAt(),
                "주문이 완료되었습니다."
        );
    }

    /**
     * 주문 단건 조회
     *
     * @param orderId
     * @return
     */
    @Transactional(readOnly = true)
    public OrderDetailResult getOrderById(UUID orderId) {
        Order order = orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
        return orderQueryMapper.toDetailDto(order); // Application Layer 내 Mapper 사용
    }

    /**
     * 주문 전체 조회
     *
     * @return
     */
    @Transactional(readOnly = true)
    public List<OrderListResult> getAllOrders() {
        List<Order> orders = orderRepository.findAllWithOrderProducts();
        return orderQueryMapper.toListDtos(orders);
    }

    /**
     * 허브별 주문 조회
     */
    @Transactional(readOnly = true)
    public List<OrderListResult> getOrdersByHubId(UUID hubId, Long userId) {

        // 1. 허브 존재 여부 확인
        if (!hubQueryPort.existsByHubId(hubId)) {
            throw new NotFoundException("허브", hubId);
        }

        // 2. 사용자 정보조회 및 권한체크
        UserAuthorizationInfo userInfo = userQueryPort.getUserAuthorizationInfo(userId);
        if (userInfo == null) {
            throw new NotFoundException(userId);
        }
        orderPermissionValidator.validateReadByHub(userId, hubId);

        // 4. 해당 허브의 주문 조회
        List<Order> orders = orderDomainService.getOrdersByHub(hubId);

        // 6. DTO 변환 및 반환
        return orderQueryMapper.toListDtos(orders);
    }



    /**
     * 주문 수정
     *
     * @param orderId
     * @param command
     * @return
     */
    public OrderUpdateResult updateOrder(UUID orderId, UpdateOrderCommand command) {
        Order order = orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        order.updateRequestNote(command.getRequestNote());

        order.getOrderProducts().stream()
                .filter(op -> op.getProductId().equals(command.getProductId()))
                .findFirst()
                .orElseThrow(() -> new OrderNotFoundException(command.getProductId()))
                .updateQuantity(command.getTransferQuantity());

        // Service에서 DTO 만들지 않고 Result만 반환
        return OrderUpdateResult.builder()
                .orderId(order.getId())         // Response에 필요한 주문 아이디
                .updatedBy(order.getUpdatedBy())    // 수정한 유저
                .updatedAt(order.getUpdatedAt())
                .build();
    }

    /**
     * 주문 삭제
     *
     * @param command
     * @return
     */
    public OrderDeleteResult deleteOrder(DeleteOrderCommand command) {
        Order order = orderRepository.findByOrderId(command.getOrderId())
                .orElseThrow(() -> new OrderNotFoundException(command.getOrderId()));

        List<OrderProduct> orderProducts = orderProductRepository.findAllByOrderId(order.getId());
        LocalDateTime now = LocalDateTime.now();

        orderProducts.forEach(p -> p.markAsDeleted(now));
        order.markAsDeleted(now);

        orderRepository.save(order);
        orderProductRepository.saveAll(orderProducts);

        return OrderDeleteResult.builder()
                .orderId(order.getId())
                .deletedBy(order.getDeletedBy())
                .deletedAt(order.getDeletedAt())
                .build();
    }
}
