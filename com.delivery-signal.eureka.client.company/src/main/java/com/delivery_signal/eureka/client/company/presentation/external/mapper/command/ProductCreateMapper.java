package com.delivery_signal.eureka.client.company.presentation.external.mapper.command;

import com.delivery_signal.eureka.client.company.application.command.CreateProductCommand;
import com.delivery_signal.eureka.client.company.presentation.external.dto.request.ProductCreateRequestDto;

public class ProductCreateMapper {

    public static CreateProductCommand toCommand(ProductCreateRequestDto dto,Long userId) {
        return CreateProductCommand.builder()
                .userId(userId)
                .productName(dto.getName())
                .price(dto.getPrice())
                .build();
    }
}
