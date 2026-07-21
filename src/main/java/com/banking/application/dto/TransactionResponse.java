package com.banking.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record TransactionResponse(

        UUID transactionId,

        String transactionType,

        BigDecimal amount,

        String accountNumber,

        String status,

        LocalDateTime createdAt

) {}
