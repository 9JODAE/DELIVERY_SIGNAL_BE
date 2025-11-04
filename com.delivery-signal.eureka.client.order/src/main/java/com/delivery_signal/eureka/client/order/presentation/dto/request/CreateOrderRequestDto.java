package com.delivery_signal.eureka.client.order.presentation.dto.request;

import com.delivery_signal.eureka.client.order.application.dto.response.OrderSummaryResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@Getter
@NoArgsConstructor
public class CreateOrderRequestDto {

    @Schema(
            description = "공급업체 회사 UUID",
            example = "550e8400-e29b-41d4-a716-446655440000"
    )
    private String supplierCompanyId;

    @Schema(
            description = "수신업체 회사 UUID",
            example = "a4c59a2c-efb2-4a5a-8b91-38d8d2e4e8e7"
    )
    private String receiverCompanyId;

    @Schema(description = "요청 메모", example = "빠른 배송 부탁드립니다.")
    private String requestNote;
    private List<OrderSummaryResponseDto> orderProducts;

    public CreateOrderRequestDto(String supplierCompanyId, String receiverCompanyId, String requestNote, List<OrderSummaryResponseDto> orderProducts) {
        this.supplierCompanyId = supplierCompanyId;
        this.receiverCompanyId = receiverCompanyId;
        this.requestNote = requestNote;
        this.orderProducts = orderProducts;
    }
}
