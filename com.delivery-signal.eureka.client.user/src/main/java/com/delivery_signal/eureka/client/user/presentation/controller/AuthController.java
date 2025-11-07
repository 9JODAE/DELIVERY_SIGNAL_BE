package com.delivery_signal.eureka.client.user.presentation.controller;

import com.delivery_signal.eureka.client.user.application.service.AuthService;
import com.delivery_signal.eureka.client.user.presentation.dto.ApiResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/open-api/v1/auth") // gateway: open-api|api/v1/auth/**
public class AuthController {
    private final AuthService authService;

    @GetMapping("/signIn")
    public ResponseEntity<ApiResponse<?>> createAuthToken(@RequestParam String user_id) {
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(new AuthResponse(authService.createAccessToken(user_id))));

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class AuthResponse {
        private String accessToken;
    }
}
