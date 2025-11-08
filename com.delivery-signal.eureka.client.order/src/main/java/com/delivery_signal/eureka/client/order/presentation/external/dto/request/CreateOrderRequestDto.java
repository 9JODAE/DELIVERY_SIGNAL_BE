package com.delivery_signal.eureka.client.order.presentation.external.dto.request;

import com.delivery_signal.eureka.client.order.application.dto.response.OrderSummaryRequestDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class CreateOrderRequestDto {

    @Schema(description = "공급업체", example = "d8b2f5a0-0ac3-4f72-8c43-bb9149a1c9a2")
    private UUID supplierCompanyId;

    @Schema(description = "수령업체", example = "3e7b1b40-6c29-4f55-9210-1e9380b23db4")
    private UUID receiverCompanyId;

    @Schema(description = "요청 메모", example = "빠른 배송 부탁드립니다.")
    private String requestNote;
    private List<OrderSummaryRequestDto> orderProducts;

}
