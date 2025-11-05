package com.example.internaltransfers.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.example.internaltransfers.dto.TransactionSubmissionRequest;
import com.example.internaltransfers.entity.Account;
import com.example.internaltransfers.entity.TransactionLog;
import com.example.internaltransfers.exception.AccountNotFoundException;
import com.example.internaltransfers.exception.InsufficientFundsException;
import com.example.internaltransfers.repository.AccountRepository;
import com.example.internaltransfers.repository.TransactionLogRepository;
import com.example.internaltransfers.service.impl.TransactionServiceImpl;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @Mock
    private AccountRepository accountRepository;
    @Mock
    private TransactionLogRepository transactionLogRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private Account source;
    private Account destination;
    private TransactionSubmissionRequest request;

    @BeforeEach
    void setup() {
        source = Account.builder().accountId(1L).balance(new BigDecimal("200.00")).build();
        destination = Account.builder().accountId(2L).balance(new BigDecimal("50.00")).build();
        request = TransactionSubmissionRequest.builder()
                .sourceAccountId(1L)
                .destinationAccountId(2L)
                .amount(new BigDecimal("75.00"))
                .build();
    }

    @Test
    void submitTransaction_success_updatesBalancesAndLogs() {
        when(accountRepository.findAccountByAccountId(1L)).thenReturn(Optional.of(source));
        when(accountRepository.findAccountByAccountId(2L)).thenReturn(Optional.of(destination));

        transactionService.submitTransaction(request);

        assertEquals(new BigDecimal("125.00"), source.getBalance());
        assertEquals(new BigDecimal("125.00"), destination.getBalance());
        verify(accountRepository).save(source);
        verify(accountRepository).save(destination);
        ArgumentCaptor<TransactionLog> logCaptor = ArgumentCaptor.forClass(TransactionLog.class);
        verify(transactionLogRepository).save(logCaptor.capture());
        TransactionLog log = logCaptor.getValue();
        assertEquals(1L, log.getSourceAccountId());
        assertEquals(2L, log.getDestinationAccountId());
        assertEquals(new BigDecimal("75.00"), log.getAmount());
    }

    @Test
    void submitTransaction_insufficientFunds_throws() {
        source.setBalance(new BigDecimal("10.00"));
        when(accountRepository.findAccountByAccountId(1L)).thenReturn(Optional.of(source));
        when(accountRepository.findAccountByAccountId(2L)).thenReturn(Optional.of(destination));

        InsufficientFundsException ex = assertThrows(InsufficientFundsException.class, () -> transactionService.submitTransaction(request));
        assertTrue(ex.getMessage().contains("Insufficient funds in source account 1"));
        verify(transactionLogRepository, never()).save(any());
    }

    @Test
    void submitTransaction_sameAccount_throws() {
        TransactionSubmissionRequest bad = TransactionSubmissionRequest.builder()
                .sourceAccountId(5L)
                .destinationAccountId(5L)
                .amount(new BigDecimal("10.00"))
                .build();
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> transactionService.submitTransaction(bad));
        assertTrue(ex.getMessage().contains("Source and destination"));
        verifyNoInteractions(accountRepository);
        verifyNoInteractions(transactionLogRepository);
    }

    @Test
    void submitTransaction_sourceNotFound_throws() {
        when(accountRepository.findAccountByAccountId(1L)).thenReturn(Optional.empty());
        AccountNotFoundException ex = assertThrows(AccountNotFoundException.class, () -> transactionService.submitTransaction(request));
        assertTrue(ex.getMessage().contains("Source account not found"));
    }

    @Test
    void submitTransaction_destinationNotFound_throws() {
        when(accountRepository.findAccountByAccountId(1L)).thenReturn(Optional.of(source));
        when(accountRepository.findAccountByAccountId(2L)).thenReturn(Optional.empty());
        AccountNotFoundException ex = assertThrows(AccountNotFoundException.class, () -> transactionService.submitTransaction(request));
        assertTrue(ex.getMessage().contains("Destination account not found"));
    }
}
