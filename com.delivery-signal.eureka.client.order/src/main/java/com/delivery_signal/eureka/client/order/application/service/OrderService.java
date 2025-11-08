package com.delivery_signal.eureka.client.order.application.service;

import com.delivery_signal.eureka.client.order.application.command.CreateOrderCommand;
import com.delivery_signal.eureka.client.order.application.command.DeleteOrderCommand;
import com.delivery_signal.eureka.client.order.application.command.OrderProductCommand;
import com.delivery_signal.eureka.client.order.application.command.UpdateOrderCommand;
import com.delivery_signal.eureka.client.order.application.dto.request.CreateDeliveryRequestDto;
import com.delivery_signal.eureka.client.order.application.dto.response.*;
import com.delivery_signal.eureka.client.order.application.mapper.OrderQueryMapper;
import com.delivery_signal.eureka.client.order.application.port.out.*;
import com.delivery_signal.eureka.client.order.common.NotFoundException;
import com.delivery_signal.eureka.client.order.domain.entity.Order;
import com.delivery_signal.eureka.client.order.domain.entity.OrderProduct;
import com.delivery_signal.eureka.client.order.domain.exception.OrderNotFoundException;
import com.delivery_signal.eureka.client.order.domain.repository.OrderProductRepository;
import com.delivery_signal.eureka.client.order.domain.repository.OrderRepository;
import com.delivery_signal.eureka.client.order.domain.service.OrderDomainService;
import com.delivery_signal.eureka.client.order.domain.vo.company.CompanyInfo;
import com.delivery_signal.eureka.client.order.domain.vo.delivery.DeliveryCreatedInfo;
import com.delivery_signal.eureka.client.order.domain.vo.hub.HubStockInfo;
import com.delivery_signal.eureka.client.order.domain.vo.product.ProductInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class OrderService {

    //요청으로 인해 외부 msa의 쓰기사용
    private final DeliveryCommandPort deliveryCommandPort;

    //요청으로 인해 외부 msa의 읽기사용
    private final HubQueryPort HubQueryPort;
    private final CompanyQueryPort companyQueryPort;
    private final ProductQueryPort productQueryPort;

    private final OrderDomainService orderDomainService;
    private final OrderQueryMapper orderQueryMapper;
    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;

    public OrderService(DeliveryCommandPort deliveryCommandPort,
                        CompanyQueryPort companyQueryPort,
                        HubQueryPort HubQueryPort,
                        ProductQueryPort productQueryPort,
                        OrderDomainService orderDomainService,
                        OrderQueryMapper orderQueryMapper,
                        OrderRepository orderRepository,
                        OrderProductRepository orderProductRepository) {
        this.deliveryCommandPort = deliveryCommandPort;
        this.companyQueryPort = companyQueryPort;
        this.HubQueryPort = HubQueryPort;
        this.productQueryPort = productQueryPort;
        this.orderDomainService = orderDomainService;
        this.orderQueryMapper = orderQueryMapper;
        this.orderRepository = orderRepository;
        this.orderProductRepository = orderProductRepository;
    }


    /**
     * 주문 생성 -> 배송생성요청
     *
     * @param command 입력된 주문 정보
     * @return 주문 결과
     */
    public OrderCreateResponseDto createOrderAndSendDelivery(CreateOrderCommand command) {

//        //유저 검증, 유저 쪽 추가되면 리팩토링 예정
//        if (!userQueryPort.isUserApproved(jwtToken)) {
//            throw new NotFoundException(jwtToken);
//        }

        // 상품 ID 리스트
        List<UUID> productIds = command.getProducts().stream()
                .map(OrderProductCommand::getProductId)
                .toList();

        // 상품 정보 조회 (한 번에)
        List<ProductInfo> productInfos = productQueryPort.getProducts(productIds);

        // 상품 검증: 각 상품이 해당 업체에 속하는지 확인
        for (ProductInfo p : productInfos) {
            if (!p.getCompanyId().equals(command.getSupplierCompanyId())) {
                throw new NotFoundException("상품",p.getProductId());
            }
        }

        // 공급업체 검증 및 상품 검증 각 상품이 해당 업체에 속하는지 확인
        for (ProductInfo p : productInfos) {
            if (!p.getCompanyId().equals(command.getSupplierCompanyId())) {
                throw new NotFoundException("상품",p.getProductId());
            }
        }

        // 업체 존재 여부 검증 및 상품 정보, 업체의 담당 허브 Id 등 가져옴.
        CompanyInfo supplier = companyQueryPort.getCompanyById(command.getSupplierCompanyId());
        CompanyInfo receiver = companyQueryPort.getCompanyById(command.getReceiverCompanyId());

        // 허브에게 상품 재고 요청
        List<HubStockInfo> productStocks = HubQueryPort.getStockQuantities(productIds);

        //내가 가진 상품과 수량을 Map리스트화
        Map<UUID, Integer> productQuantities = command.getProducts().stream()
                .collect(Collectors.toMap(
                        OrderProductCommand::getProductId,
                        OrderProductCommand::getQuantity
                ));

        //상품 재고 검증
        productStocks.stream()
                .filter(s -> s.getStockQuantity() < productQuantities.getOrDefault(s.getProductId(), 0))
                .findFirst()
                .ifPresent(s -> {
                    throw new IllegalStateException("재고 부족: " + s.getProductId());
                });


        // 주문 상품 도메인 생성
        UUID deliveryId = UUID.randomUUID();
        List<OrderProduct> orderProducts = productInfos.stream()
                .map(info -> OrderProduct.builder()
                        .productId(info.getProductId())
                        .productName(info.getProductName())
                        .productPriceAtOrder(info.getPrice())
                        .transferQuantity(productQuantities.get(info.getProductId()))
                        .build())
                .toList();

        //주문 생성
        Order order = orderDomainService.createOrder(
                supplier.getHubId(), receiver.getHubId(), command.getRequestNote(), orderProducts, deliveryId);

        // 주문 저장
        orderRepository.save(order);

        //배송 생성 요청
        CreateDeliveryRequestDto deliveryRequest = CreateDeliveryRequestDto.builder()
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

        return new OrderCreateResponseDto(order.getId(), order.getCreatedBy(), order.getCreatedAt(), "주문이 완료되었습니다.");
    }

    /**
     * 주문 단건 조회
     *
     * @param orderId
     * @return
     */
    @Transactional(readOnly = true)
    public OrderDetailResponseDto getOrderById(UUID orderId) {
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
    public List<OrderListResponseDto> getAllOrders() {
        List<Order> orders = orderRepository.findAllWithOrderProducts();
        return orderQueryMapper.toListDtos(orders);
    }

    /**
     * 주문 수정
     *
     * @param orderId
     * @param command
     * @return
     */
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

    /**
     * 주문 삭제
     *
     * @param command
     * @return
     */
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
