package com.higor.cs50x.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginResponse(
        String message,
        String email,
        String token,
        String tokenType,
        @Schema(description = "Token expiration time in minutes: 30 minutes")
        Long expiresIn
)
{
    public LoginResponse(Long expiresIn, String email, String token) {
        this(
                "Login successful.",
                email,
                token,
                "Bearer",
                expiresIn
        );
    }
}
