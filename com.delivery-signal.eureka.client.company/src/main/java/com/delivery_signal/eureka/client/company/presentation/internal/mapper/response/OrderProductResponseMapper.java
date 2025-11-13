package com.delivery_signal.eureka.client.company.presentation.internal.mapper.response;

import com.delivery_signal.eureka.client.company.application.result.OrderProductResult;
import com.delivery_signal.eureka.client.company.presentation.internal.dto.response.OrderProductResponseDto;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Order 서비스 통신용 상품 Mapper
 */
public class OrderProductResponseMapper {

    public static List<OrderProductResponseDto> toResponseList(List<OrderProductResult> results) {
        return results.stream()
                .map(result -> OrderProductResponseDto.builder()
                        .productId(result.getProductId())
                        .companyId(result.getCompanyId())
                        .productName(result.getProductName())
                        .price(result.getPrice())
                        .build())
                .collect(Collectors.toList());
    }
}
