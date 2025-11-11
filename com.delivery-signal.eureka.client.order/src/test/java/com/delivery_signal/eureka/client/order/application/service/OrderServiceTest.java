package com.delivery_signal.eureka.client.order.application.service;

import com.delivery_signal.eureka.client.order.application.command.CreateOrderCommand;
import com.delivery_signal.eureka.client.order.application.command.OrderProductCommand;
import com.delivery_signal.eureka.client.order.application.result.OrderCreateResult;
import com.delivery_signal.eureka.client.order.application.port.out.*;
import com.delivery_signal.eureka.client.order.application.validator.OrderPermissionValidator;
import com.delivery_signal.eureka.client.order.domain.entity.Order;
import com.delivery_signal.eureka.client.order.domain.entity.OrderProduct;
import com.delivery_signal.eureka.client.order.domain.repository.OrderRepository;
import com.delivery_signal.eureka.client.order.domain.service.OrderDomainService;
import com.delivery_signal.eureka.client.order.domain.vo.company.CompanyInfo;
import com.delivery_signal.eureka.client.order.domain.vo.delivery.DeliveryCreatedInfo;
import com.delivery_signal.eureka.client.order.domain.vo.product.ProductInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private ProductQueryPort productQueryPort;

    @Mock
    private CompanyQueryPort companyQueryPort;

    @Mock
    private HubQueryPort hubQueryPort;

    @Mock
    private DeliveryCommandPort deliveryCommandPort;

    @Mock
    private HubCommandPort hubCommandPort;

    @Mock
    private OrderDomainService orderDomainService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserQueryPort userQueryPort; // 사용자 승인 검증

    @Mock
    private OrderPermissionValidator orderPermissionValidator; // 권한 검증

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * 주문 생성 정상 흐름 확인 + 권한 체크 포함
     * - userId 기반 권한 검증 호출
     * - UserQueryPort.isUserApproved 호출
     * - 상품/업체/재고 조회, 도메인 주문 생성, 배송 생성 정상 동작
     * - 기존 검증 호출 모두 유지
     */
    @Test
    void createOrderAndSendDelivery_success_withPermissionCheck() {
        // given
        Long userId = 123L;
        UUID supplierCompanyId = UUID.randomUUID();
        UUID receiverCompanyId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        UUID hubId = UUID.randomUUID();

        //Application 계층용 Command 객체 직접 생성
        CreateOrderCommand command = CreateOrderCommand.builder()
                .supplierCompanyId(supplierCompanyId)
                .receiverCompanyId(receiverCompanyId)
                .requestNote("테스트 주문")
                .userId(userId)
                .products(List.of(
                        new OrderProductCommand(productId, 3) // Command 전용 타입 사용
                ))
                .build();

        // 권한 체크 mock
        doNothing().when(orderPermissionValidator).validateCreate(userId);

        // 유저 승인 여부 mock
        when(userQueryPort.isUserApproved(userId)).thenReturn(true);

        // 상품 조회 mock
        when(productQueryPort.getProducts(anyList()))
                .thenReturn(List.of(
                        ProductInfo.builder()
                                .productId(productId)
                                .companyId(supplierCompanyId)
                                .productName("테스트상품")
                                .price(BigDecimal.valueOf(1000))
                                .build()
                ));

        // 공급/수령 업체 조회 mock
        when(companyQueryPort.getCompanyById(supplierCompanyId))
                .thenReturn(new CompanyInfo(supplierCompanyId, hubId, "서울시 강남구"));
        when(companyQueryPort.getCompanyById(receiverCompanyId))
                .thenReturn(new CompanyInfo(receiverCompanyId, hubId, "서울시 송파구"));

        // 허브 재고 조회 mock
        when(hubQueryPort.getStockQuantities(anyList()))
                .thenReturn(Map.of(productId, 100));

        // 도메인 주문 생성 mock
        when(orderDomainService.createOrder(any(), any(), any(), anyList(), any()))
                .thenReturn(Order.builder()
                        .supplierCompanyId(supplierCompanyId)
                        .receiverCompanyId(receiverCompanyId)
                        .orderProducts(List.of(
                                OrderProduct.builder()
                                        .productId(productId)
                                        .productName("테스트상품")
                                        .transferQuantity(3)
                                        .build()
                        ))
                        .build()
                );

        // 배송 생성 mock
        when(deliveryCommandPort.createDelivery(any()))
                .thenReturn(DeliveryCreatedInfo.builder()
                        .message("배송 요청 완료")
                        .build());

        // when
        OrderCreateResult response = orderService.createOrderAndSendDelivery(command);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getMessage()).isEqualTo("주문이 완료되었습니다.");

        //권한 검증 수행 여부 확인 (주문 생성 전 사용자 권한 검증 로직이 실행되었는지 확인)
        verify(orderPermissionValidator).validateCreate(userId);
        //사용자 승인 여부 검증 (해당 유저가 주문을 생성할 수 있는 승인 상태인지 확인)
        verify(userQueryPort).isUserApproved(userId);
        //상품 정보 조회 호출 확인 (주문 생성 시 필요한 상품 상세 정보를 외부 서비스로부터 조회했는지 확인)
        verify(productQueryPort).getProducts(anyList());
        //공급업체 / 수령업체 정보 조회 호출 확인 (각 업체의 허브나 주소 등 배송 경로 계산에 필요한 정보 조회)
        verify(companyQueryPort, times(2)).getCompanyById(any());
        //주문 데이터 저장 호출 확인 (생성된 주문 엔티티가 실제로 저장소에 저장되었는지 검증)
        verify(orderRepository).save(any());
        //배송 생성 호출 확인 (주문 생성 이후 배송 요청이 외부 배송 서비스에 전달되었는지 검증)
        verify(deliveryCommandPort).createDelivery(any());
        //허브 재고 조회 호출 확인 (주문 수량만큼 허브의 재고가 충분한지 확인했는지 검증)
        verify(hubQueryPort).getStockQuantities(anyList());
        //허브 재고 차감 호출 확인 (주문 수량만큼 허브 재고를 실제로 감소시켰는지 검증)
        verify(hubCommandPort).decreaseStock(command.getProducts());

    }
}
