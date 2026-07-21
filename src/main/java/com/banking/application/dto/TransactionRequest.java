package com.banking.application.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record TransactionRequest(

        @NotBlank(message = "Transaction type is required")
        String transactionType,

        @NotNull(message = "Amount is required")
        @Positive(message = "Amount must be greater than zero")
        BigDecimal amount,


        @NotBlank(message = "Account number is required")
        String accountNumber,


        String status
) {
}
