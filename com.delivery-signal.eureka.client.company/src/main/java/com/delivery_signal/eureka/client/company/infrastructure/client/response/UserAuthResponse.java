package com.delivery_signal.eureka.client.company.infrastructure.client.response;

import java.util.UUID;

public record UserAuthResponse (
        Long userId,
        String role,
        String organization,
        UUID organizationId
){
}



