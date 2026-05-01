package com.higor.cs50x.dto.response;

import com.higor.cs50x.model.entity.Account;
import com.higor.cs50x.model.entity.CheckingAccount;
import com.higor.cs50x.model.enums.AccountType;

import java.math.BigDecimal;

public record MyAccountResponse(
        String branch,
        String number,
        AccountType type,
        BigDecimal balance
)
{
    public MyAccountResponse(Account account)
    {
        this(
                account.getBranch(),
                account.getNumber(),
                (account instanceof CheckingAccount) ? AccountType.CHECKING : AccountType.SAVINGS,
                account.getBalance()
        );
    }
}
