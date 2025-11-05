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
public class TransactionSubmissionRequest {
    @NotNull
    @JsonProperty("source_account_id")
    private Long sourceAccountId;
    @NotNull
    @JsonProperty("destination_account_id")
    private Long destinationAccountId;
    @NotNull
    @JsonProperty("amount")
    private BigDecimal amount;
}
