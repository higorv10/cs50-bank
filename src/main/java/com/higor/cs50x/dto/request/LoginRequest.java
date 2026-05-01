package com.higor.cs50x.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "{email.required}")
        @Email(message = "{invalid.email}")
        String email,

        @NotBlank(message = "{password.required}")
        String password
)
{
}
