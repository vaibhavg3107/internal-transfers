package com.example.internaltransfers.service;

import com.example.internaltransfers.dto.AccountCreateRequest;
import com.example.internaltransfers.entity.Account;

import java.math.BigDecimal;

public interface AccountService {

    void createAccount(AccountCreateRequest request);

    Account getAccount(Long accountId);
}
