package com.delivery_signal.eureka.client.user.application.dto.request;

public record CreateUserLoginRequest(
    String username,
    String password
){}

