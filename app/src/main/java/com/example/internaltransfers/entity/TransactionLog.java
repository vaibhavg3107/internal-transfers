package com.example.internaltransfers.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "transaction_logs")
public class TransactionLog extends BaseEntity {

    private Long sourceAccountId;
    private Long destinationAccountId;
    private BigDecimal amount;
}
