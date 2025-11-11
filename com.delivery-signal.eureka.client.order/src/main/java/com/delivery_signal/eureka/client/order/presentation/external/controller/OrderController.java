package com.delivery_signal.eureka.client.order.presentation.external.controller;

import com.delivery_signal.eureka.client.order.application.command.CreateOrderCommand;
import com.delivery_signal.eureka.client.order.application.command.DeleteOrderCommand;
import com.delivery_signal.eureka.client.order.application.command.OrderCancelCommand;
import com.delivery_signal.eureka.client.order.application.command.UpdateOrderCommand;
import com.delivery_signal.eureka.client.order.application.result.*;
import com.delivery_signal.eureka.client.order.application.service.OrderService;
import com.delivery_signal.eureka.client.order.presentation.external.dto.request.CreateOrderRequestDto;
import com.delivery_signal.eureka.client.order.presentation.external.dto.request.UpdateOrderRequestDto;
import com.delivery_signal.eureka.client.order.presentation.external.dto.response.*;
import com.delivery_signal.eureka.client.order.presentation.external.mapper.command.CreateOrderMapper;
import com.delivery_signal.eureka.client.order.presentation.external.mapper.command.OrderCancelMapper;
import com.delivery_signal.eureka.client.order.presentation.external.mapper.command.OrderDeleteMapper;
import com.delivery_signal.eureka.client.order.presentation.external.mapper.response.OrderResponseMapper;
import com.delivery_signal.eureka.client.order.presentation.external.mapper.command.UpdateOrderMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/orders")
@Tag(name = "Order API", description = "주문 관련 API")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "주문 생성", description = "새로운 주문을 등록합니다.")
    @PostMapping
    public ResponseEntity<OrderCreateResponseDto> createOrder(
            @RequestBody CreateOrderRequestDto requestDto,
            @RequestHeader(value = "x-user-id", required = false) Long userId) {

        CreateOrderCommand command = CreateOrderMapper.toCommand(requestDto, userId);
        OrderCreateResult result = orderService.createOrderAndSendDelivery(command);
        OrderCreateResponseDto responseDto = OrderResponseMapper.toCreateResponse(result);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    // 단건 조회 (Read one)
    @Operation(summary = "주문 조회", description = "주문 조회(개별)")
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDetailResponseDto> getOrderById(
            @PathVariable UUID orderId,
            @RequestHeader(value = "x-user-id", required = false) Long userId) {
        OrderDetailResult result = orderService.getOrderById(orderId, userId);
        OrderDetailResponseDto responseDto = OrderResponseMapper.toDetailResponse(result);
        return ResponseEntity.ok(responseDto);
    }

    // 전체 조회 (Read all)
    @Operation(summary = "주문 전체 조회", description = "관리자용")
    @GetMapping
    public ResponseEntity<List<OrderListResponseDto>> getAllOrders(
            @RequestHeader(value = "x-user-id", required = false) Long userId) {
        List<OrderListResult> result = orderService.getAllOrders(userId);
        List<OrderListResponseDto> responseDto = result.stream()
                .map(OrderResponseMapper::toListResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDto);
    }

    // 허브별 주문 조회 (관리자, 허브 담당자용)
    @Operation(summary = "허브별 주문 조회", description = "관리자, 허브 담당자용")
    @GetMapping("/hub/{hubId}")
    public ResponseEntity<List<OrderListResponseDto>> getAllOrdersByHubId(
            @PathVariable UUID hubId,
            @RequestHeader(value = "x-user-id", required = false) Long userId) {

        List<OrderListResult> result = orderService.getOrdersByHubId(hubId, userId);

        List<OrderListResponseDto> responseDto = result.stream()
                .map(OrderResponseMapper::toListResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseDto);
    }


    // 수정 (Update)
    @Operation(summary = "주문 수정", description = "주문 수정")
    @PutMapping("/{orderId}")
    public ResponseEntity<OrderUpdateResponseDto> updateOrder(
            @PathVariable UUID orderId,
            @RequestBody UpdateOrderRequestDto requestDto,
            @RequestHeader(value = "x-user-id", required = false) Long userId
    ) {
        UpdateOrderCommand command = UpdateOrderMapper.toCommand(orderId, requestDto, userId);
        OrderUpdateResult result = orderService.updateOrder(command);
        OrderUpdateResponseDto response =OrderResponseMapper.toUpdateResponse(result);
        return ResponseEntity.ok(response);
    }

    // 삭제 (Delete)
    @Operation(summary = "주문 삭제", description = "주문 삭제")
    @DeleteMapping("/{orderId}")
    public ResponseEntity<OrderDeleteResponseDto> deleteOrder(
            @PathVariable UUID orderId,
            @RequestHeader(value = "x-user-id", required = false) Long userId) {
        DeleteOrderCommand command = OrderDeleteMapper.toCommand(orderId, userId);
        OrderDeleteResult result = orderService.deleteOrder(command);
        OrderDeleteResponseDto responseDto = OrderResponseMapper.toDeleteResponse(result);
        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "주문 취소", description = "주문 및 연관된 배송을 취소합니다.")
    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<OrderCancelResponseDto> cancelOrder(
            @PathVariable UUID orderId,
            @RequestHeader(value = "x-user-id", required = false) Long userId) {

        OrderCancelCommand command = OrderCancelMapper.toCommand(orderId, userId);
        OrderCancelResult result = orderService.cancelOrder(command);
        OrderCancelResponseDto response = OrderResponseMapper.toCancelResponse(result);
        return ResponseEntity.ok(response);
    }

}
