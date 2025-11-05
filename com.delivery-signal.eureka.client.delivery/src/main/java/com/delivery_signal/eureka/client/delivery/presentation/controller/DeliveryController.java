package com.delivery_signal.eureka.client.delivery.presentation.controller;

import com.delivery_signal.eureka.client.delivery.application.service.DeliveryService;
import com.delivery_signal.eureka.client.delivery.domain.model.Delivery;
import com.delivery_signal.eureka.client.delivery.presentation.dto.request.DeliveryCreateRequest;
import com.delivery_signal.eureka.client.delivery.presentation.dto.request.DeliveryManagerRegisterRequest;
import com.delivery_signal.eureka.client.delivery.presentation.dto.response.DeliveryCreateResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/deliveries")
public class DeliveryController {

    private final DeliveryService deliveryService;

    // API Gateway에서 인증 후, USER ID와 ROLE을 헤더에 담아 전달
    private static final String USER_ID_HEADER = "X-User-Id";
    private static final String USER_ROLE_HEADER = "X-User-Role";
    // 담당 허브 ID
    private static final String USER_HUB_ID_HEADER = "X-User-Hub-Id";

    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @PostMapping
    public ResponseEntity<DeliveryCreateResponse> createDelivery(
        @Valid @RequestBody DeliveryCreateRequest request,
        @RequestHeader(USER_ID_HEADER) Long currUserId,
        @RequestHeader(USER_ROLE_HEADER) String role
    ) {
        DeliveryCreateResponse response = deliveryService.createDelivery(currUserId, request, role);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}

//FeignClient 인터페이스 정의: OrderService, HubRouteService 등 다른 서비스의 API를 호출하기 위한 FeignClient를 정의합니다.

//

//DTO 정의: 주문 생성 요청을 받아 배송 엔티티를 생성하고, 다른 서비스에 데이터를 요청할 때 사용할 DTO를 정의합니다.

//

//서비스 로직 구현:

//

//트랜잭션 관리: @Transactional을 사용하여 주문 생성, 배송 엔티티 생성, 재고 감소 등이 하나의 논리적 단위로 묶이도록 합니다. 실패 시 롤백.

//

//서비스 간 통신: FeignClient를 사용하여 주문 정보, 허브 경로 정보를 가져옵니다.

//

//배송 경로 생성: 주문 정보와 허브 경로 정보를 바탕으로 전체 배송 경로 (DeliveryRouteLog)를 최초에 모두 생성합니다. (경로 모델 선택에 따라 로직 복잡도 달라짐. 여기서는 P2P를 가정)