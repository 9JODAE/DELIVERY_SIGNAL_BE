package com.delivery_signal.eureka.client.order.application.service;

import com.delivery_signal.eureka.client.order.application.command.CreateOrderCommand;
import com.delivery_signal.eureka.client.order.application.command.OrderProductCommand;
import com.delivery_signal.eureka.client.order.application.dto.response.OrderCreateResponseDto;
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

        // 직접 Command 생성 (presentation 의존 제거)
        CreateOrderCommand command = CreateOrderCommand.builder()
                .userId(userId)
                .supplierCompanyId(supplierCompanyId)
                .receiverCompanyId(receiverCompanyId)
                .requestNote("테스트 주문")
                .products(List.of(
                        OrderProductCommand.builder()
                                .productId(productId)
                                .quantity(3)
                                .build()
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
        OrderCreateResponseDto response = orderService.createOrderAndSendDelivery(command);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getMessage()).isEqualTo("주문이 완료되었습니다.");

        // 권한 체크, 유저 승인 호출 확인
        verify(orderPermissionValidator, times(1)).validateCreate(userId);
        verify(userQueryPort, times(1)).isUserApproved(userId);

        // 기존 검증 호출 유지

        // 상품 조회 호출 확인
        verify(productQueryPort, times(1)).getProducts(anyList());
        // 공급/수령 업체 조회 호출 확인
        verify(companyQueryPort, times(2)).getCompanyById(any());
        // 주문 저장 호출 확인
        verify(orderRepository, times(1)).save(any());
        // 배송 생성 호출 확인
        verify(deliveryCommandPort, times(1)).createDelivery(any());
        // 허브 재고 조회 호출 확인
        verify(hubQueryPort, times(1)).getStockQuantities(anyList());
        // 허브 재고 차감 호출 확인
        verify(hubCommandPort, times(1)).decreaseStock(command.getProducts());
    }
}
