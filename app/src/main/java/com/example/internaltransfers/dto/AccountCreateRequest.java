package com.example.internaltransfers.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountCreateRequest {
    @NotNull
    @JsonProperty("account_id")
    private Long accountId;
    @NotNull
    @JsonProperty("initial_balance")
    private BigDecimal initialBalance;
}
