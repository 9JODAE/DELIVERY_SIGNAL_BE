package com.delivery_signal.eureka.client.order.presentation.external.controller;

import com.delivery_signal.eureka.client.order.application.command.CreateOrderCommand;
import com.delivery_signal.eureka.client.order.application.command.DeleteOrderCommand;
import com.delivery_signal.eureka.client.order.application.command.UpdateOrderCommand;
import com.delivery_signal.eureka.client.order.application.dto.response.*;
import com.delivery_signal.eureka.client.order.application.service.OrderService;
import com.delivery_signal.eureka.client.order.presentation.external.dto.request.CreateOrderRequestDto;
import com.delivery_signal.eureka.client.order.presentation.external.dto.request.UpdateOrderRequestDto;
import com.delivery_signal.eureka.client.order.presentation.mapper.CreateOrderMapper;
import com.delivery_signal.eureka.client.order.presentation.mapper.OrderDeleteMapper;
import com.delivery_signal.eureka.client.order.presentation.mapper.UpdateOrderMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/orders")
@Tag(name = "Order API", description = "주문 관련 API")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "주문 생성", description = "새로운 주문을 등록합니다.")
    @PostMapping
    public ResponseEntity<OrderCreateResponseDto> createOrder(@RequestBody CreateOrderRequestDto requestDto) {

        CreateOrderCommand command = CreateOrderMapper.toCommand(requestDto);

        OrderCreateResponseDto responseDto = orderService.createOrderAndSendDelivery(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    // 단건 조회 (Read one)
    @Operation(summary = "주문 조회", description = "주문 조회(개별)")
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDetailResponseDto> getOrderById(@PathVariable UUID orderId) {
        OrderDetailResponseDto responseDto = orderService.getOrderById(orderId);
        return ResponseEntity.ok(responseDto);
    }

    // 전체 조회 (Read all)
    @Operation(summary = "주문 전체 조회", description = "관리자용")
    @GetMapping
    public ResponseEntity<List<OrderListResponseDto>> getAllOrders() {
        List<OrderListResponseDto> responseDto = orderService.getAllOrders();
        return ResponseEntity.ok(responseDto);
    }


    // 수정 (Update)
    @Operation(summary = "주문 수정", description = "주문 수정")
    @PutMapping("/{orderId}")
    public ResponseEntity<OrderUpdateResponseDto> updateOrder(@PathVariable UUID orderId, @RequestBody UpdateOrderRequestDto requestDto) {
        UpdateOrderCommand command = UpdateOrderMapper.toCommand(orderId, requestDto);

        OrderUpdateResponseDto responseDto = orderService.updateOrder(orderId, command);
        return ResponseEntity.ok(responseDto);
    }

    // 삭제 (Delete)
    @Operation(summary = "주문 삭제", description = "주문 삭제")
    @DeleteMapping("/{orderId}")
    public ResponseEntity<OrderDeleteResponseDto> deleteOrder(@PathVariable UUID orderId) {
        DeleteOrderCommand command = OrderDeleteMapper.toCommand(orderId);
        OrderDeleteResponseDto requestDto = orderService.deleteOrder(command);
        return ResponseEntity.ok(requestDto);
    }
}
