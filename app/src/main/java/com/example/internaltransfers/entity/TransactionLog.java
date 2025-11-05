package com.example.internaltransfers.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

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
