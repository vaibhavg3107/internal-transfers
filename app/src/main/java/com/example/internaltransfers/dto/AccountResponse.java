package com.example.internaltransfers.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountResponse {

    @JsonProperty("account_id")
    private Long accountId;
    @JsonProperty("balance")
    private BigDecimal balance;
}
