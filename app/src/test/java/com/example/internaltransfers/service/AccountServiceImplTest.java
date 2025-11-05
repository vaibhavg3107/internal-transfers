package com.example.internaltransfers.service;

import com.example.internaltransfers.dto.AccountCreateRequest;
import com.example.internaltransfers.entity.Account;
import com.example.internaltransfers.exception.AccountAlreadyExistException;
import com.example.internaltransfers.exception.AccountNotFoundException;
import com.example.internaltransfers.repository.AccountRepository;
import com.example.internaltransfers.service.impl.AccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    @Test
    void createAccount_success_savesEntity() {
        AccountCreateRequest request = getAccountCreateRequest(123L, "100.50");
        when(accountRepository.existsByAccountId(123L)).thenReturn(false);

        accountService.createAccount(request);

        ArgumentCaptor<Account> captor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository).save(captor.capture());
        Account savedAccount = captor.getValue();
        assertEquals(123L, savedAccount.getAccountId());
        assertEquals(new BigDecimal("100.50"), savedAccount.getBalance());
    }

    @Test
    void createAccount_negativeBalance_throws() {
        AccountCreateRequest request = getAccountCreateRequest(124L, "-100");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> accountService.createAccount(request));
        assertEquals("Initial balance cannot be negative", ex.getMessage());
        verifyNoInteractions(accountRepository);
    }

    @Test
    void createAccount_duplicate_throws() {
        AccountCreateRequest request = getAccountCreateRequest(123L, "100.50");
        when(accountRepository.existsByAccountId(123L)).thenReturn(true);

        AccountAlreadyExistException ex = assertThrows(AccountAlreadyExistException.class, () -> accountService.createAccount(request));

        assertTrue(ex.getMessage().contains("Account already exists"));
        verify(accountRepository, never()).save(any());
    }

    @Test
    void getAccount_success() {
        Account account = Account.builder().accountId(999L).balance(new BigDecimal("10.00")).build();
        when(accountRepository.findAccountByAccountId(999L)).thenReturn(Optional.of(account));

        Account outputAccount = accountService.getAccount(999L);

        assertEquals(999L, outputAccount.getAccountId());
        assertEquals(new BigDecimal("10.00"), outputAccount.getBalance());
    }

    @Test
    void getAccount_notFound_throws() {
        when(accountRepository.findAccountByAccountId(5L)).thenReturn(Optional.empty());

        AccountNotFoundException ex = assertThrows(AccountNotFoundException.class, () -> accountService.getAccount(5L));

        assertTrue(ex.getMessage().contains("Account not found for accountId 5"));
    }

    private AccountCreateRequest getAccountCreateRequest(Long accountId, String amount) {
        return AccountCreateRequest.builder()
                .accountId(accountId)
                .initialBalance(new BigDecimal(amount))
                .build();
    }
}
