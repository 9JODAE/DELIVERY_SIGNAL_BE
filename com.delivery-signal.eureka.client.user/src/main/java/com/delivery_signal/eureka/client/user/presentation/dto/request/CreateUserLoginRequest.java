package com.delivery_signal.eureka.client.user.presentation.dto.request;

public record CreateUserLoginRequest(
    String username,
    String password
){}

