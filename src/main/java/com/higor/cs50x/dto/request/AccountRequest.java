package com.higor.cs50x.dto.request;

import com.higor.cs50x.model.enums.AccountType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record AccountRequest(
        @NotNull(message = "{type.required}")
        @Schema(description = "{account.type}")
        AccountType type,

        @NotBlank(message = "{password.required}")
        @Pattern(regexp = "\\d{10}", message = "{invalid.password}")
        String password
)
{
}
