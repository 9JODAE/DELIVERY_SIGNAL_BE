package com.delivery_signal.eureka.client.company.presentation.internal.mapper.response;

import com.delivery_signal.eureka.client.company.application.result.OrderCompanyResult;
import com.delivery_signal.eureka.client.company.presentation.internal.dto.response.OrderCompanyResponseDto;

/**
 * Order 서비스 통신용 업체 Mapper
 */
public class OrderCompanyResponseMapper {

    public static OrderCompanyResponseDto toResponse(OrderCompanyResult result) {
        return OrderCompanyResponseDto.builder()
                .companyId(result.getCompanyId())
                .hubId(result.getHubId())
                .address(result.getAddress())
                .build();
    }
}

