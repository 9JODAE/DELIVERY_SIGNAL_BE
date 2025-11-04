package com.delivery_signal.eureka.client.order.presentation;

import com.delivery_signal.eureka.client.order.application.service.OrderService;
import com.delivery_signal.eureka.client.order.presentation.dto.request.CreateOrderRequestDto;
import com.delivery_signal.eureka.client.order.presentation.dto.response.OrderCreateResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@Tag(name = "Order API", description = "주문 관련 API")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "주문 생성", description = "새로운 주문을 등록합니다.")
    @PostMapping
    public ResponseEntity<OrderCreateResponseDto> createOrder(@RequestBody CreateOrderRequestDto dto) {
        OrderCreateResponseDto response = orderService.createOrder(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

//    // 단건 조회 (Read one)
//    @Operation(summary = "주문 조회", description = "주문 조회(개별)")
//    @GetMapping("/{id}")
//    public ResponseEntity<OrderDetailResponseDto> getOrderById(@PathVariable UUID uuid) {
//        OrderDetailResponseDto response = orderService.getOrderById(uuid);
//        return ResponseEntity.ok(response);
//    }
//
//    // 전체 조회 (Read all)
//    @GetMapping
//    public ResponseEntity<List<OrderResponseDto>> getAllOrders() {
//        List<OrderResponseDto> responses = orderService.getAllOrders();
//        return ResponseEntity.ok(responses);
//    }
//
//    // 수정 (Update)
//    @PutMapping("/{id}")
//    public ResponseEntity<OrderResponseDto> updateOrder(@PathVariable Long id, @RequestBody CreateOrderRequestDto dto) {
//        OrderResponseDto response = orderService.updateOrder(id, dto);
//        return ResponseEntity.ok(response);
//    }
//
//    // 삭제 (Delete)
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
//        orderService.deleteOrder(id);
//        return ResponseEntity.noContent().build();
//    }
}
