package com.delivery_signal.eureka.client.company.presentation.external.mapper.command;

import com.delivery_signal.eureka.client.company.application.command.UpdateProductCommand;
import com.delivery_signal.eureka.client.company.presentation.external.dto.request.ProductUpdateRequestDto;

import java.util.UUID;

public class ProductUpdateMapper {

    public static UpdateProductCommand toCommand(UUID productId, ProductUpdateRequestDto dto, Long userId) {
        return UpdateProductCommand.builder()
                .productId(productId)
                .userId(userId)
                .price(dto.getPrice())
                .productName(dto.getName())
                .build();
    }
}
