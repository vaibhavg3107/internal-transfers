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
@Table(name = "accounts")
public class Account extends BaseEntity {

    @Column(updatable = false, nullable = false, unique = true)
    private Long accountId;
    private BigDecimal balance;
}
