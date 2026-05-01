package com.higor.cs50x.dto.response;

import com.higor.cs50x.model.entity.Account;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record WithdrawResponse(
        String message,
        BigDecimal currentBalance,
        LocalDateTime timestamp
)
{
    public WithdrawResponse(Account account) {
        this(
                "Withdrawal successful.",
                account.getBalance(),
                LocalDateTime.now()
        );
    }
}
