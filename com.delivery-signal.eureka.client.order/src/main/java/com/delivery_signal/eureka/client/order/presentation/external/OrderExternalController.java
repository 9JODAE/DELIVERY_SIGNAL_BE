package com.delivery_signal.eureka.client.order.presentation.external;


import com.delivery_signal.eureka.client.order.application.service.OrderService;
import com.delivery_signal.eureka.client.order.domain.entity.Order;
import com.delivery_signal.eureka.client.order.presentation.dto.response.OrderDetailResponseDto;
import com.delivery_signal.eureka.client.order.presentation.external.dto.OrderPongResponseDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/orders")
@Tag(name = "외부 통신용 API", description = "외부 통신용 API")
@RequiredArgsConstructor
public class OrderExternalController {

    @GetMapping
    public ResponseEntity<OrderPongResponseDto> ping(
            @RequestParam(required = false) String from // optional
    ) {
        log.info("Ping received : {}", from != null ? from : "unknown");

        OrderPongResponseDto response = new OrderPongResponseDto(
                "order-service 통신이 제대로 되고 있습니다!",
                "OK",
                Instant.now()
        );
        return ResponseEntity.ok(response);
    }


}
