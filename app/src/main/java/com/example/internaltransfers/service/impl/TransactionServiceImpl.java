package com.example.internaltransfers.service.impl;

import com.example.internaltransfers.dto.TransactionSubmissionRequest;
import com.example.internaltransfers.entity.Account;
import com.example.internaltransfers.entity.TransactionLog;
import com.example.internaltransfers.exception.AccountNotFoundException;
import com.example.internaltransfers.exception.InsufficientFundsException;
import com.example.internaltransfers.repository.AccountRepository;
import com.example.internaltransfers.repository.TransactionLogRepository;
import com.example.internaltransfers.service.TransactionService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Log4j2
public class TransactionServiceImpl implements TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionLogRepository transactionLogRepository;

    public TransactionServiceImpl(AccountRepository accountRepository, TransactionLogRepository transactionLogRepository) {
        this.accountRepository = accountRepository;
        this.transactionLogRepository = transactionLogRepository;
    }

    @Override
    @Transactional
    public void submitTransaction(TransactionSubmissionRequest request) {
        log.info("Receive transaction submission request: {}", request);
        validateTransactionSubmissionRequest(request);
        Account source = accountRepository.findAccountByAccountId(request.getSourceAccountId())
                .orElseThrow(() -> new AccountNotFoundException("Source account not found"));
        Account destination = accountRepository.findAccountByAccountId(request.getDestinationAccountId())
                .orElseThrow(() -> new AccountNotFoundException("Destination account not found"));
        BigDecimal transactionAmount = request.getAmount();
        if (source.getBalance().compareTo(transactionAmount) < 0) {
            throw new InsufficientFundsException("Insufficient funds in source account " + source.getAccountId());
        }
        source.setBalance(source.getBalance().subtract(transactionAmount));
        destination.setBalance(destination.getBalance().add(transactionAmount));
        TransactionLog transactionLog = TransactionLog.builder()
                .sourceAccountId(request.getSourceAccountId())
                .destinationAccountId(request.getDestinationAccountId())
                .amount(request.getAmount())
                .build();
        accountRepository.save(source);
        accountRepository.save(destination);
        transactionLogRepository.save(transactionLog);
        log.info("Transaction processed successfully");
    }

    private void validateTransactionSubmissionRequest(TransactionSubmissionRequest request) {
        if (request.getSourceAccountId().equals(request.getDestinationAccountId())) {
            throw new IllegalArgumentException("Source and destination cannot be the same");
        }
        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
    }
}
