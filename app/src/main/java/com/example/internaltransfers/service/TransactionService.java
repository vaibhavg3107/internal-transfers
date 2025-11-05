package com.example.internaltransfers.service;

import com.example.internaltransfers.dto.TransactionSubmissionRequest;

public interface TransactionService {

    void submitTransaction(TransactionSubmissionRequest request);
}
