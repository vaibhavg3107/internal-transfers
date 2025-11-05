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
