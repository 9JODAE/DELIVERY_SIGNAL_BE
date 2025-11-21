package com.delivery_signal.eureka.client.order.application.service;

import com.delivery_signal.eureka.client.order.application.command.CreateOrderCommand;
import com.delivery_signal.eureka.client.order.application.command.OrderProductCommand;
import com.delivery_signal.eureka.client.order.application.result.OrderCreateResult;
import com.delivery_signal.eureka.client.order.application.port.out.*;
import com.delivery_signal.eureka.client.order.application.validator.OrderPermissionValidator;
import com.delivery_signal.eureka.client.order.domain.entity.Order;
import com.delivery_signal.eureka.client.order.domain.entity.OrderProduct;
import com.delivery_signal.eureka.client.order.domain.service.OrderDomainService;
import com.delivery_signal.eureka.client.order.domain.vo.company.CompanyInfo;
import com.delivery_signal.eureka.client.order.domain.vo.delivery.DeliveryCreatedInfo;
import com.delivery_signal.eureka.client.order.domain.vo.product.ProductInfo;
import com.delivery_signal.eureka.client.order.domain.vo.user.UserAuthorizationInfo;
import com.delivery_signal.eureka.client.order.domain.vo.user.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * OrderService 단위테스트
 * Port, Validator, DomainService를 Mock으로 구성하여 외부 의존성 제거
 */
class OrderServiceTest {

    @InjectMocks
    private OrderService orderService; // 실제 테스트 대상 서비스

    // ==================== Mock Port/Service ====================
    @Mock private CompanyQueryPort companyQueryPort;
    @Mock private HubQueryPort hubQueryPort;
    @Mock private DeliveryCommandPort deliveryCommandPort;
    @Mock private HubCommandPort hubCommandPort;
    @Mock private OrderDomainService orderDomainService;
    @Mock private UserQueryPort userQueryPort;
    @Mock private OrderPermissionValidator orderPermissionValidator;
    @Mock private OrderCommandPort orderCommandPort;

    // ==================== 테스트용 데이터 ====================
    private Long userId;
    private UUID supplierCompanyId;
    private UUID receiverCompanyId;
    private UUID supplierHubId;
    private UUID receiverHubId;
    private UUID productId;
    private CreateOrderCommand command;

    // ==================== 공통 초기화 ====================
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        userId = 123L;
        supplierCompanyId = UUID.randomUUID();
        receiverCompanyId = UUID.randomUUID();
        supplierHubId = UUID.randomUUID();
        receiverHubId = UUID.randomUUID();
        productId = UUID.randomUUID();

        command = CreateOrderCommand.builder()
                .supplierCompanyId(supplierCompanyId)
                .receiverCompanyId(receiverCompanyId)
                .userId(userId)
                .requestNote("테스트 주문")
                .products(List.of(new OrderProductCommand(productId, 3)))
                .recipient("홍길동")
                .recipientSlackId("slack124")
                .build();

        // 사용자 권한 mock (Validator 내부에서 사용)
        when(userQueryPort.getUserAuthorizationInfo(userId))
                .thenReturn(UserAuthorizationInfo.builder()
                        .userId(userId)
                        .role(UserRole.SUPPLIER_MANAGER)
                        .organization("COMPANY")
                        .organizationId(UUID.randomUUID())
                        .build());

        // 업체 조회 mock
        when(companyQueryPort.getCompanyById(supplierCompanyId))
                .thenReturn(new CompanyInfo(supplierCompanyId, supplierHubId, "서울시 강남구"));
        when(companyQueryPort.getCompanyById(receiverCompanyId))
                .thenReturn(new CompanyInfo(receiverCompanyId, receiverHubId, "서울시 송파구"));

        // 상품 조회 mock
        when(companyQueryPort.getProducts(anyList()))
                .thenReturn(List.of(ProductInfo.builder()
                        .productId(productId)
                        .companyId(supplierCompanyId)
                        .productName("테스트상품")
                        .price(BigDecimal.valueOf(1000))
                        .build()));

        // 허브 재고 조회 mock
        when(hubQueryPort.getStockQuantities(anyList()))
                .thenReturn(Map.of(productId, 100));

        // 도메인 주문 생성 mock (최신 구조 반영)
        when(orderDomainService.createOrder(
                any(UUID.class),
                any(UUID.class),
                any(UUID.class),
                any(UUID.class),
                anyString(),
                anyList(),   // productInfos
                anyMap()    // productQuantities
        )).thenAnswer(invocation -> {

            UUID deliveryId = invocation.getArgument(7);

            Order order = Order.of(
                    supplierCompanyId,
                    receiverCompanyId,
                    supplierHubId,
                    receiverHubId,
                    "테스트 주문",
                    BigDecimal.valueOf(3000),
                    deliveryId
            );

            OrderProduct op = OrderProduct.create(
                    order,
                    productId,
                    "테스트상품",
                    BigDecimal.valueOf(1000),
                    3
            );

            order.getOrderProducts().add(op);
            return order;
        });

        // 배송 생성 mock
        when(deliveryCommandPort.createDelivery(any()))
                .thenReturn(DeliveryCreatedInfo.builder()
                        .message("배송 요청 완료")
                        .build());
    }

    // ==================== 테스트 ====================

    // 권한 및 승인 검증
    @Test
    void validateUserPermissionAndApproval() {
        orderService.createOrderAndSendDelivery(command);

        verify(orderPermissionValidator).validateCreate(userId);
    }

    // 상품 조회 및 재고 확인
    @Test
    void getProductsAndStockQuantities() {
        orderService.createOrderAndSendDelivery(command);

        verify(companyQueryPort).getProducts(anyList());
        verify(hubQueryPort).getStockQuantities(anyList());
    }

    // 업체 조회 및 허브 ID 추출
    @Test
    void getCompanyInfo_hubIdExtraction() {
        orderService.createOrderAndSendDelivery(command);

        verify(companyQueryPort).getCompanyById(supplierCompanyId);
        verify(companyQueryPort).getCompanyById(receiverCompanyId);
    }

    // 도메인 주문 생성 및 저장
    @Test
    void createOrderDomainEntity() {
        orderService.createOrderAndSendDelivery(command);

        verify(orderDomainService).createOrder(
                eq(supplierCompanyId),
                eq(receiverCompanyId),
                eq(supplierHubId),
                eq(receiverHubId),
                eq("테스트 주문"),
                anyList(),
                anyMap()
        );

        verify(orderCommandPort).save(any(Order.class));
    }

    // 배송 생성 호출 및 메시지 검증
    @Test
    void createDeliveryCall() {
        OrderCreateResult result = orderService.createOrderAndSendDelivery(command);

        verify(deliveryCommandPort).createDelivery(any());
        assertThat(result.getMessage()).isEqualTo("주문이 완료되었습니다.");
    }

    // 허브 재고 차감 확인
    @Test
    void deductHubStock() {

        when(companyQueryPort.getCompanyById(command.getSupplierCompanyId()))
                .thenReturn(new CompanyInfo(command.getSupplierCompanyId(), supplierHubId, "서울시 강남구"));

        when(companyQueryPort.getCompanyById(command.getReceiverCompanyId()))
                .thenReturn(new CompanyInfo(command.getReceiverCompanyId(), receiverHubId, "서울시 송파구"));

        orderService.createOrderAndSendDelivery(command);

        Map<UUID, Integer> expectedMap = command.getProducts().stream()
                .collect(Collectors.toMap(
                        OrderProductCommand::getProductId,
                        OrderProductCommand::getQuantity
                ));

        verify(hubCommandPort).deductStocks(eq(supplierHubId), eq(expectedMap));
    }
}
