package com.example.internaltransfers.repository;

import com.example.internaltransfers.entity.TransactionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TransactionLogRepository extends JpaRepository<TransactionLog, UUID> {
}
