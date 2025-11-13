package com.delivery_signal.eureka.client.order.presentation.external.dto.request;

import com.delivery_signal.eureka.client.order.presentation.external.dto.response.OrderProductResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequestDto {

    @Schema(description = "공급업체", example = "5eaa1d9a-bb51-4e12-93de-4a24aeb1b001")
    private UUID supplierCompanyId;

    @Schema(description = "수령업체", example = "5eaa1d9a-bb51-4e12-93de-4a24aeb1b002")
    private UUID receiverCompanyId;

    @Schema(description = "요청 메모", example = "빠른 배송 부탁드립니다.")
    private String requestNote;
    private List<OrderProductResponseDto> orderProducts;

    @Schema(description = "주문자이름", example = "김철수")
    private String recipient;          //주문자 이름 (임의로 쓰기 가능)

    @Schema(description = "요청 SlackId", example = "slack124")
    private String recipientSlackId;   // 슬랙id (배송 정보 받을 slack id이므로 이쪽도 임의로 쓰기 가능)

}
