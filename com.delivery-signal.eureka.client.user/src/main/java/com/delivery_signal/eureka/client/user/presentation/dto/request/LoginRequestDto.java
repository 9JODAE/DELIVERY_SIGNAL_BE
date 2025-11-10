package com.delivery_signal.eureka.client.user.presentation.dto.request;

import lombok.Getter;
import lombok.Setter;

public record LoginRequestDto (
    String username,
    String password
){}

