package com.example.internaltransfers.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
