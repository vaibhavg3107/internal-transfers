package com.example.internaltransfers.repository;

import com.example.internaltransfers.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import jakarta.persistence.LockModeType;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findAccountByAccountId(Long accountId);

    Boolean existsByAccountId(Long accountId);
}
