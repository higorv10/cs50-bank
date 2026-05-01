package com.higor.cs50x.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record WithdrawRequest(
        @NotBlank(message = "{branch.required}")
        @Size(min = 3, max = 3, message = "{invalid.branch}" )
        String branch,

        @NotBlank(message = "{account.required}")
        @Size(min = 6, max = 6, message = "{invalid.account}")
        String accountNumber,

        @NotNull(message = "{amount.required}")
        @Positive(message = "{invalid.amount}")
        BigDecimal amount,

        @NotBlank(message = "{password.required}")
        String password
)
{
}
