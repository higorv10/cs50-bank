package com.higor.cs50x.service;

import com.higor.cs50x.dto.request.AccountRequest;
import com.higor.cs50x.dto.request.DepositRequest;
import com.higor.cs50x.dto.request.WithdrawRequest;
import com.higor.cs50x.dto.response.DepositResponse;
import com.higor.cs50x.dto.response.MyAccountResponse;
import com.higor.cs50x.dto.response.WithdrawResponse;
import com.higor.cs50x.exceptions.BusinessException;
import com.higor.cs50x.exceptions.InvalidCredentialsException;
import com.higor.cs50x.exceptions.MyAccountNotFoundException;
import com.higor.cs50x.model.adapter.UserPrincipal;
import com.higor.cs50x.model.entity.*;
import com.higor.cs50x.model.enums.AccountType;
import com.higor.cs50x.repositoy.AccountRepository;
import com.higor.cs50x.repositoy.UserRepository;
import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AccountService
{
    private final AccountRepository repository;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    private String generateBranchNumber()
    {
        Random random = new Random();
        int branch = random.nextInt(100) + 100;
        return String.valueOf(branch);
    }

    private String generateAccountNumber()
    {
        Random random = new Random();
        String number;
        boolean exists;
        do
        {
            number = String.valueOf(random.nextInt(900000) + 100000);
            exists = repository.existsByNumber(number);
        }
        while (exists);

        return number;
    }

    @Transactional
    public Account createAccount(
            AccountRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal)
    {
        /* Converts the Enum type to the specific entity class.
        This allows the JPQL filter to be discriminated using the TYPE() function.*/
        Class<? extends Account> targetClass = (request.type() == AccountType.CHECKING)
                ? CheckingAccount.class
                : SavingsAccount.class;

        if(repository.existsByEmailAndType(userPrincipal.getUsername(), targetClass))
        {
            throw new BusinessException("You already have an account of this type: " + request.type());
        }

        User user = userRepository.findByEmail(userPrincipal.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Authentication is required."));

        String branchNumber = generateBranchNumber();
        String accountNumber = generateAccountNumber();

        Account newAccount;
        if (request.type() == AccountType.CHECKING)
        {
             newAccount = new CheckingAccount(
                    new BigDecimal("500.00"));
        }
        else
        {
            newAccount = new SavingsAccount(
                    new BigDecimal("0.03")
            );
        }

        newAccount.setUser(user);
        newAccount.setBranch(branchNumber);
        newAccount.setNumber(accountNumber);
        newAccount.setBalance(BigDecimal.ZERO);
        newAccount.setPassword(encoder.encode(request.password()));

        return repository.save(newAccount);
    }

    @Transactional
    public List<MyAccountResponse> getMyAccounts(@AuthenticationPrincipal UserPrincipal userPrincipal)
    {
        List<Account> accounts = repository.findByUserEmail(userPrincipal.getUsername());

        return accounts.stream()
                .map(MyAccountResponse::new)
                .toList();
    }

    public DepositResponse makeDeposit(DepositRequest request)
    {
        Account account = repository.findByBranchAndNumber(request.branch(), request.accountNumber())
                .orElseThrow(() -> new MyAccountNotFoundException("Recipient not found"));

        BigDecimal currentBalance = account.getBalance();
        BigDecimal newBalance = currentBalance.add(request.amount());

        account.setBalance(newBalance);
        repository.save(account);

        return new DepositResponse(account);
    }

    @Transactional
    public WithdrawResponse makeWithdraw(UserPrincipal userPrincipal, WithdrawRequest request)
    {
        Account account = repository.findByUserEmailAndBranchAndNumber(
                userPrincipal.getUsername(),
                request.branch(),
                request.accountNumber()).orElseThrow(() ->
                new MyAccountNotFoundException("Account not found") );

        if (!encoder.matches(request.password(), account.getPassword()))
        {
            throw new InvalidCredentialsException("Incorrect password");
        }

        BigDecimal extraLimit = BigDecimal.ZERO;
        if(account instanceof CheckingAccount checking)
        {
            extraLimit = checking.getOverdraftLimit();
        }

        BigDecimal totalAvailable = account.getBalance().add(extraLimit);

        if(request.amount().compareTo(totalAvailable) > 0)
        {
            throw new BusinessException("Insufficient funds, even with overdraft limit");
        }

        BigDecimal currentBalance = account.getBalance();
        BigDecimal newBalance = currentBalance.subtract(request.amount());

        account.setBalance(newBalance);
        repository.save(account);

        return new WithdrawResponse(account);
    }
}
