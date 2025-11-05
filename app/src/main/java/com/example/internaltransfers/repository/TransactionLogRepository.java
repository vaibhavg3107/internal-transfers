package com.example.internaltransfers.repository;

import com.example.internaltransfers.entity.TransactionLog;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionLogRepository extends JpaRepository<TransactionLog, UUID> {
}
