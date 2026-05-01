package com.higor.cs50x.controller;

import com.higor.cs50x.config.SecurityConfig;
import com.higor.cs50x.dto.request.AccountRequest;
import com.higor.cs50x.dto.request.DepositRequest;
import com.higor.cs50x.dto.request.WithdrawRequest;
import com.higor.cs50x.dto.response.AccountResponse;
import com.higor.cs50x.dto.response.DepositResponse;
import com.higor.cs50x.dto.response.MyAccountResponse;
import com.higor.cs50x.dto.response.WithdrawResponse;
import com.higor.cs50x.model.entity.Account;
import com.higor.cs50x.model.adapter.UserPrincipal;
import com.higor.cs50x.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@Tag(name = "accounts", description = "Controller for creating and managing accounts")
@SecurityRequirement(name = SecurityConfig.SECURITY)
@RequiredArgsConstructor
public class AccountController
{
    private final AccountService service;

    @Operation(summary = "Account creation", description = "Method responsible for creating the account")
    @ApiResponse(responseCode = "201", description = "Account created successfully")
    @ApiResponse(responseCode = "409", description = "You already have an account of this type")
    @ApiResponse(responseCode = "401", description = "You are not authenticated")
    @ApiResponse(responseCode = "500", description = "Internal Server Error")
    @PostMapping("/create")
    public ResponseEntity<AccountResponse> createAccount(
            @Valid @RequestBody AccountRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    )
    {
        Account account = service.createAccount(request, userPrincipal);
        return ResponseEntity.status(HttpStatus.CREATED).body(new AccountResponse(account));
    }

    @GetMapping("/me")
    @Operation(summary = "My Account", description = "Method used to restore the current user's accounts")
    @ApiResponse(responseCode = "200", description = "Accounts displayed successfully")
    @ApiResponse(responseCode = "401", description = "Authentication Required")
    @ApiResponse(responseCode = "500", description = "Internal Server Error")
    public ResponseEntity<List<MyAccountResponse>> getMyAccounts(@AuthenticationPrincipal UserPrincipal userPrincipal)
    {
        List<MyAccountResponse> accounts = service.getMyAccounts(userPrincipal);
        return ResponseEntity.ok(accounts);
    }

    @PostMapping("/deposit")
    @Operation(summary = "Deposit", description = "Method used to make a deposit")
    @ApiResponse(responseCode = "200", description = "Deposit successfully completed.")
    @ApiResponse(responseCode = "404", description = "Account or branch not found")
    @ApiResponse(responseCode = "500", description = "Internal Server Error")
    public ResponseEntity<DepositResponse> Deposit(@Valid @RequestBody DepositRequest request)
    {
       DepositResponse response = service.makeDeposit(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/withdraw")
    @Operation(summary = "Withdraw", description = "Method used to make a withdraw")
    @ApiResponse(responseCode = "200", description = "Successful withdrawal")
    @ApiResponse(responseCode = "401", description = "Incorrect password")
    @ApiResponse(responseCode = "404", description = "Account or branch not found")
    @ApiResponse(responseCode = "409", description = "Insufficient balance")
    @ApiResponse(responseCode = "500", description = "Internal Server Error")
    public ResponseEntity<WithdrawResponse> Withdraw(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody WithdrawRequest request
    )
    {
        WithdrawResponse response = service.makeWithdraw(userPrincipal, request);
        return ResponseEntity.ok(response);
    }
}
