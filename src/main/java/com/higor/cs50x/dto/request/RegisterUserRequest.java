package com.higor.cs50x.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterUserRequest(

        @NotBlank(message = "{name.required}")
        @Size(min = 2, max = 255, message = "{invalid.size.name}")
        @Pattern(regexp = "^[A-Za-zÀ-ú\\s]+$", message = "{invalid.name}")
        String name,

        @NotBlank(message = "{cpf.required}")
        @Pattern(regexp = "\\d{11}", message = "{invalid.cpf}")
        String cpf,

        @NotBlank(message = "{cellphone.required}")
        @Pattern(regexp = "\\d{10,15}", message = "{invalid.cellphone}")
        String cellphone,

        @NotBlank(message = "{email.required}")
        @Email(message = "{invalid.email}")
        String email,

        @NotBlank(message = "{password.required}")
        @Size(min = 8, max = 20, message = "{invalid.password.register}")
        String password
)
{
}
