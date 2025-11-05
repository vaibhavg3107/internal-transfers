package com.example.internaltransfers.service.impl;

import com.example.internaltransfers.dto.AccountCreateRequest;
import com.example.internaltransfers.entity.Account;
import com.example.internaltransfers.exception.AccountAlreadyExistException;
import com.example.internaltransfers.exception.AccountNotFoundException;
import com.example.internaltransfers.repository.AccountRepository;
import com.example.internaltransfers.service.AccountService;
import java.math.BigDecimal;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Log4j2
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    @Transactional
    public void createAccount(AccountCreateRequest request) {
        log.info("Creating account {}", request);
        validateAccountCreationRequest(request);
        Account account = Account.builder()
                .accountId(request.getAccountId())
                .balance(request.getInitialBalance())
                .build();
        accountRepository.save(account);
        log.info("Account with account id {} created successfully", account.getAccountId());
    }

    private void validateAccountCreationRequest(AccountCreateRequest request) {
        if (request.getInitialBalance().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative");
        }
        if (accountRepository.existsByAccountId(request.getAccountId())) {
            throw new AccountAlreadyExistException("Account already exists with account id " + request.getAccountId());
        }
    }

    @Override
    public Account getAccount(Long accountId) {
        return accountRepository.findAccountByAccountId(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found for accountId " + accountId));
    }
}
