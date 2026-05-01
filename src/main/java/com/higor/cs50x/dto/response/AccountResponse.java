package com.higor.cs50x.dto.response;

import com.higor.cs50x.model.entity.Account;

public record AccountResponse(
        String message,
        String branch,
        String number
)
{
    public AccountResponse(Account account)
    {
        this(
                "Account created successfully",
                account.getBranch(),
                account.getNumber()
        );
    }
}
