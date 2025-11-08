package com.delivery_signal.eureka.client.order.application.service;

import com.delivery_signal.eureka.client.order.application.command.CreateOrderCommand;
import com.delivery_signal.eureka.client.order.application.dto.response.OrderCreateResponseDto;
import com.delivery_signal.eureka.client.order.application.dto.response.OrderSummaryRequestDto;
import com.delivery_signal.eureka.client.order.application.port.out.CompanyQueryPort;
import com.delivery_signal.eureka.client.order.application.port.out.DeliveryCommandPort;
import com.delivery_signal.eureka.client.order.application.port.out.HubQueryPort;
import com.delivery_signal.eureka.client.order.application.port.out.ProductQueryPort;
import com.delivery_signal.eureka.client.order.domain.entity.Order;
import com.delivery_signal.eureka.client.order.domain.entity.OrderProduct;
import com.delivery_signal.eureka.client.order.domain.repository.OrderRepository;
import com.delivery_signal.eureka.client.order.domain.service.OrderDomainService;
import com.delivery_signal.eureka.client.order.domain.vo.company.CompanyInfo;
import com.delivery_signal.eureka.client.order.domain.vo.delivery.DeliveryCreatedInfo;
import com.delivery_signal.eureka.client.order.domain.vo.hub.HubStockInfo;
import com.delivery_signal.eureka.client.order.domain.vo.product.ProductInfo;
import com.delivery_signal.eureka.client.order.presentation.external.dto.request.CreateOrderRequestDto;
import com.delivery_signal.eureka.client.order.presentation.mapper.CreateOrderMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
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
    private OrderDomainService orderDomainService;

    @Mock
    private OrderRepository orderRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createOrderAndSendDelivery_success() {
        // given
        UUID supplierCompanyId = UUID.randomUUID();
        UUID receiverCompanyId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        UUID hubId = UUID.randomUUID();

        // requestDto 더미 데이터 세팅
        CreateOrderRequestDto requestDto = CreateOrderRequestDto.builder()
                .supplierCompanyId(supplierCompanyId)
                .receiverCompanyId(receiverCompanyId)
                .requestNote("테스트 주문")
                .orderProducts(List.of(
                        OrderSummaryRequestDto.builder()
                                .productId(productId)
                                .quantity(3)
                                .build()
                ))
                .build();


        CreateOrderCommand command = CreateOrderMapper.toCommand(requestDto);

        when(productQueryPort.getProducts(anyList()))
                .thenReturn(List.of(
                        ProductInfo.builder()
                                .productId(productId)
                                .companyId(supplierCompanyId)
                                .productName("테스트상품")
                                .price(BigDecimal.valueOf(1000))
                                .build()
                ));

        when(companyQueryPort.getCompanyById(supplierCompanyId))
                .thenReturn(new CompanyInfo(
                        supplierCompanyId,
                        hubId,
                        "서울시 강남구"
                ));

        when(companyQueryPort.getCompanyById(receiverCompanyId))
                .thenReturn(new CompanyInfo(
                        receiverCompanyId,
                        hubId,
                        "서울시 송파구"
                ));
        when(hubQueryPort.getStockQuantities(Collections.singletonList(any(UUID.class))))
                .thenReturn(List.of(
                        HubStockInfo.builder()
                                .productId(productId)
                                .stockQuantity(100)
                                .build()
                ));


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


        when(deliveryCommandPort.createDelivery(any()))
                .thenReturn(DeliveryCreatedInfo.builder()
                        .message("배송 요청 완료")
                        .build());

        // when
        OrderCreateResponseDto response = orderService.createOrderAndSendDelivery(command);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getMessage()).isEqualTo("주문이 완료되었습니다.");

        // 동작 검증
        verify(productQueryPort, times(1)).getProducts(anyList());
        verify(companyQueryPort, times(2)).getCompanyById(any());
        verify(orderRepository, times(1)).save(any());
        verify(deliveryCommandPort, times(1)).createDelivery(any());
    }
}
