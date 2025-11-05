package com.example.internaltransfers.repository;

import com.example.internaltransfers.entity.Account;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findAccountByAccountId(Long accountId);

    Boolean existsByAccountId(Long accountId);
}
