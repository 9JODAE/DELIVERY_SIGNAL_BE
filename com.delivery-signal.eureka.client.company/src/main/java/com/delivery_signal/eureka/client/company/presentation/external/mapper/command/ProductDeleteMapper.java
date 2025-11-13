package com.delivery_signal.eureka.client.company.presentation.external.mapper.command;

import com.delivery_signal.eureka.client.company.application.command.DeleteProductCommand;

import java.util.UUID;

public class ProductDeleteMapper {

    public static DeleteProductCommand toCommand(UUID productId, Long userId) {
        return DeleteProductCommand.builder()
                .productId(productId)
                .userId(userId)
                .build();
    }
}