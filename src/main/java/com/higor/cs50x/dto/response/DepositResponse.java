package com.higor.cs50x.dto.response;

import com.higor.cs50x.model.entity.Account;
import com.higor.cs50x.model.entity.CheckingAccount;
import com.higor.cs50x.model.enums.AccountType;

import java.time.LocalDateTime;

public record  DepositResponse(
        String message,
        AccountType accountType,
        LocalDateTime timestamp
)
{
    public DepositResponse(Account account) {
        this(
                "Deposit successfully completed",
                (account instanceof CheckingAccount) ? AccountType.CHECKING : AccountType.SAVINGS,
                LocalDateTime.now()
        );
    }
}
