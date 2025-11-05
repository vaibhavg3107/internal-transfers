package com.example.internaltransfers.controller;

import com.example.internaltransfers.dto.AccountCreateRequest;
import com.example.internaltransfers.dto.AccountResponse;
import com.example.internaltransfers.entity.Account;
import com.example.internaltransfers.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/accounts")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<Void> createAccount(@Valid @RequestBody AccountCreateRequest request) {
        accountService.createAccount(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<AccountResponse> getAccount(@PathVariable("accountId") Long accountId) {
        Account account = accountService.getAccount(accountId);
        return ResponseEntity.ok(new AccountResponse(account.getAccountId(), account.getBalance()));
    }
}
