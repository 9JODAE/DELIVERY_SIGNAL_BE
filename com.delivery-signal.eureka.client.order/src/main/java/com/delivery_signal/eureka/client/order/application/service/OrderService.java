package com.delivery_signal.eureka.client.order.application.service;

import com.delivery_signal.eureka.client.order.application.command.*;
import com.delivery_signal.eureka.client.order.application.mapper.OrderQueryMapper;
import com.delivery_signal.eureka.client.order.application.port.out.*;
import com.delivery_signal.eureka.client.order.application.result.*;
import com.delivery_signal.eureka.client.order.application.validator.OrderPermissionValidator;
import com.delivery_signal.eureka.client.order.common.NotFoundException;
import com.delivery_signal.eureka.client.order.domain.entity.Order;
import com.delivery_signal.eureka.client.order.domain.entity.OrderProduct;
import com.delivery_signal.eureka.client.order.domain.exception.InvalidOrderStateException;
import com.delivery_signal.eureka.client.order.domain.exception.OrderNotFoundException;
import com.delivery_signal.eureka.client.order.domain.repository.OrderProductRepository;
import com.delivery_signal.eureka.client.order.domain.service.OrderDomainService;
import com.delivery_signal.eureka.client.order.domain.vo.OrderStatus;
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

    private final DeliveryCommandPort deliveryCommandPort;
    private final HubCommandPort hubCommandPort;
    private final OrderCommandPort orderCommandPort;

    private final HubQueryPort hubQueryPort;
    private final CompanyQueryPort companyQueryPort;
    private final UserQueryPort userQueryPort;
    private final OrderQueryPort orderQueryPort;

    private final OrderDomainService orderDomainService;
    private final OrderQueryMapper orderQueryMapper;
    private final OrderProductRepository orderProductRepository;
    private final OrderPermissionValidator orderPermissionValidator;

    public OrderService(DeliveryCommandPort deliveryCommandPort, HubCommandPort hubCommandPort, OrderCommandPort orderCommandPort, HubQueryPort hubQueryPort, CompanyQueryPort companyQueryPort, UserQueryPort userQueryPort, OrderQueryPort orderQueryPort, OrderDomainService orderDomainService, OrderQueryMapper orderQueryMapper, OrderProductRepository orderProductRepository, OrderPermissionValidator orderPermissionValidator) {
        this.deliveryCommandPort = deliveryCommandPort;
        this.hubCommandPort = hubCommandPort;
        this.orderCommandPort = orderCommandPort;
        this.hubQueryPort = hubQueryPort;
        this.companyQueryPort = companyQueryPort;
        this.userQueryPort = userQueryPort;
        this.orderQueryPort = orderQueryPort;
        this.orderDomainService = orderDomainService;
        this.orderQueryMapper = orderQueryMapper;
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

        // [LOG] 사용자 권한조회 시작
        log.info("[ORDER] validateCreate(userId={}) 요청 시작", command.getUserId());
        String userRole =  orderPermissionValidator.validateCreate(command.getUserId());
        log.info("[ORDER] validateCreate 성공 -> userRole={}", userRole);


        // 상품 ID 리스트
        List<UUID> productIds = command.getProducts().stream()
                .map(OrderProductCommand::getProductId)
                .toList();
        log.info("[ORDER] 상품 ID 목록 = {}", productIds);

        // [LOG] 상품 조회 시작
        log.info("[ORDER] 회사 서비스(productInfos) 조회 시작: productIds={}", productIds);
        List<ProductInfo> productInfos = companyQueryPort.getProducts(productIds);
        log.info("[ORDER] productInfos 조회 성공: count={}", productInfos.size());


        // 공급업체 검증
        productInfos.stream()
                .filter(p -> !p.getCompanyId().equals(command.getSupplierCompanyId()))
                .findFirst()
                .ifPresent(p -> {
                    log.error("[ORDER] 상품 소속 불일치 -> productId={}, supplierCompanyId={}",
                            p.getProductId(), command.getSupplierCompanyId());
                    throw new NotFoundException("상품", p.getProductId());
                });


        // [LOG] 업체 조회
        log.info("[ORDER] 공급업체 조회 시작: {}", command.getSupplierCompanyId());
        CompanyInfo supplier = companyQueryPort.getCompanyById(command.getSupplierCompanyId());
        log.info("[ORDER] 공급업체 조회 성공 -> hub={}", supplier.getHubId());

        log.info("[ORDER] 수령업체 조회 시작: {}", command.getReceiverCompanyId());
        CompanyInfo receiver = companyQueryPort.getCompanyById(command.getReceiverCompanyId());
        log.info("[ORDER] 수령업체 조회 성공 -> hub={}", receiver.getHubId());


        // [LOG] 허브 재고 조회
        log.info("[ORDER] 허브 재고 조회 시작: 출발허브={}, productIds={}", supplier.getHubId(), productIds);
        Map<UUID, Integer> productStocks = hubQueryPort.getStockQuantities(productIds);
        log.info("[ORDER] 허브 재고 조회 성공: {}", productStocks);


        // 재고 검증
        command.getProducts().stream()
                .filter(p -> productStocks.getOrDefault(p.getProductId(), 0) < p.getQuantity())
                .findFirst()
                .ifPresent(p -> {
                    log.error("[ORDER] 재고 부족 발생 -> productId={}, 요청수량={}, 보유수량={}",
                            p.getProductId(), p.getQuantity(), productStocks.getOrDefault(p.getProductId(), 0));
                    throw new IllegalStateException("재고 부족: 상품 " + p.getProductId());
                });


        // 수량 맵 생성
        Map<UUID, Integer> productQuantityMap = command.getProducts().stream()
                .collect(java.util.stream.Collectors.toMap(
                        OrderProductCommand::getProductId,
                        OrderProductCommand::getQuantity
                ));
        log.info("[ORDER] 상품 수량 맵 생성 완료: {}", productQuantityMap);


        // 배송 ID 미리 생성
        UUID deliveryId = UUID.randomUUID();
        log.info("[ORDER] 배송ID 생성됨 = {}", deliveryId);


        // [LOG] 도메인 주문 생성
        log.info("[ORDER] 도메인 createOrder 시작");
        Order order = orderDomainService.createOrder(
                supplier.getCompanyId(),
                receiver.getCompanyId(),
                supplier.getHubId(),
                receiver.getHubId(),
                command.getRequestNote(),
                productInfos,
                productQuantityMap,
                deliveryId
        );
        log.info("[ORDER] 도메인 createOrder 성공 -> orderId={}", order.getId());


        // 주문 저장
        log.info("[ORDER] 주문 저장 시작: orderId={}", order.getId());
        orderCommandPort.save(order);
        log.info("[ORDER] 주문 저장 완료");


        // 허브 재고 차감 요청
        log.info("[ORDER] 재고 차감 요청 시작: departureHubId={}, items={}",
                order.getDepartureHubId(), command.getProducts());
        hubCommandPort.deductStocks(order.getDepartureHubId(), command.getProducts());
        log.info("[ORDER] 재고 차감 요청 완료");


        // [LOG] 배송 생성 요청
        log.info("[ORDER] 배송 생성 요청 시작: deliveryId={}, orderId={}",
                deliveryId, order.getId());

        DeliveryCreatedInfo deliveryInfo = deliveryCommandPort.createDelivery(
                CreateDeliveryCommand.builder()
                        .userId(command.getUserId())
                        .userRole(userRole)
                        .deliveryId(deliveryId)
                        .orderId(order.getId())
                        .supplierCompanyId(command.getSupplierCompanyId())
                        .receiverCompanyId(command.getReceiverCompanyId())
                        .departureHubId(supplier.getHubId())
                        .destinationHubId(receiver.getHubId())
                        .address(receiver.getAddress())
                        .recipient(command.getRecipient())
                        .recipientSlackId(command.getRecipientSlackId())
                        .build()
        );

        log.info("[ORDER] 배송 생성 성공 -> deliveryMessage={}", deliveryInfo.getMessage());


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
    public OrderDetailResult getOrderById(UUID orderId, Long userId) {
        Order order = orderQueryPort.findByOrderId(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        // 권한 검증: MASTER_ADMIN / HUB_ADMIN(자기 허브) / COMPANY_MANAGER(본인 주문)
        orderPermissionValidator.validateRead(
                userId,
                order.getDepartureHubId(), // HUB_ADMIN은 자기 허브 체크용
                order.getCreatedBy()       // COMPANY_MANAGER는 본인 주문 체크용
        );


        return orderQueryMapper.toDetailDto(order); // Application Layer 내 Mapper 사용
    }

    /**
     * 주문 전체 조회
     *
     * @return
     */
    @Transactional(readOnly = true)
    public List<OrderListResult> getAllOrders(Long userId) {
        List<Order> orders = orderQueryPort.findAllWithOrderProducts();

        // null을 전달하면 validateReadByHub에서 MASTER만 통과시키도록 설계되어 있음
        orderPermissionValidator.validateReadByHub(userId, null);

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
     * @param command
     * @return
     */
    public OrderUpdateResult updateOrder(UpdateOrderCommand command) {
        Order order = orderQueryPort.findByOrderId(command.getOrderId())
                .orElseThrow(() -> new OrderNotFoundException(command.getOrderId()));

        // 권한 검증: MASTER_ADMIN 또는 HUB_ADMIN(자기 허브)
        orderPermissionValidator.validateUpdate(command.getUserId(), order.getDepartureHubId());

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

    @Transactional
    public OrderCancelResult cancelOrder(OrderCancelCommand command) {

        // 1. 주문 조회
        Order order = orderQueryPort.findByOrderId(command.getOrderId())
                .orElseThrow(() -> new OrderNotFoundException(command.getOrderId()));

        // 2. 권한 검증
        // MASTER_ADMIN / HUB_ADMIN(자기 허브) / COMPANY_MANAGER(본인 주문)
        orderPermissionValidator.validateCancel(
                command.getUserId(),
                order.getDepartureHubId(),      // HUB_ADMIN은 허브 기준
                order.getCreatedBy()            // COMPANY_MANAGER는 본인 주문 기준
        );

        // 3. 상태 검증
        if (order.getDeletedAt() != null) {
            throw new InvalidOrderStateException("삭제된 주문은 취소할 수 없습니다.");
        }
        if (order.getStatus() == OrderStatus.CANCELED) {
            throw new InvalidOrderStateException("이미 취소된 주문입니다.");
        }

        // 4. 배송 취소 요청
        if (order.getDeliveryId() != null) {
            deliveryCommandPort.cancelDelivery(order.getDeliveryId());
        }

        // 5. 주문 취소 처리 (도메인 로직)
        order.cancel();

        // 6. 허브 재고 복구
        hubCommandPort.restoreStocks(order.getDepartureHubId(), command.getProducts());

        // 7. 주문 저장
        orderCommandPort.save(order);

        // 8. 결과 반환
        return OrderCancelResult.builder()
                .orderId(order.getId())
                .deliveryId(order.getDeliveryId())
                .message("주문 및 배송이 정상적으로 취소되었습니다.")
                .build();
    }




    /**
     * 주문 삭제
     *
     * @param command
     * @return
     */
    public OrderDeleteResult deleteOrder(DeleteOrderCommand command) {
        Order order = orderQueryPort.findByOrderId(command.getOrderId())
                .orElseThrow(() -> new OrderNotFoundException(command.getOrderId()));

        // 권한검증: MASTER_ADMIN 또는 HUB_ADMIN(자기 허브)
        orderPermissionValidator.validateDelete(command.getUserId(), order.getDepartureHubId());

        List<OrderProduct> orderProducts = orderProductRepository.findAllByOrderId(order.getId());
        LocalDateTime now = LocalDateTime.now();

        orderProducts.forEach(p -> p.markAsDeleted(now));
        order.markAsDeleted(now);

        orderCommandPort.save(order);
        orderProductRepository.saveAll(orderProducts);

        return OrderDeleteResult.builder()
                .orderId(order.getId())
                .deletedBy(order.getDeletedBy())
                .deletedAt(order.getDeletedAt())
                .build();
    }
}
