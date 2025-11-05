package com.example.internaltransfers.controller;

import com.example.internaltransfers.dto.TransactionSubmissionRequest;
import com.example.internaltransfers.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<Void> submit(@Valid @RequestBody TransactionSubmissionRequest request) {
        transactionService.submitTransaction(request);
        return ResponseEntity.ok().build();
    }
}
