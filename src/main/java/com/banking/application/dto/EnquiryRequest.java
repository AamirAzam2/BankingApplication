package com.banking.application.dto;

import jakarta.validation.constraints.NotBlank;

public record EnquiryRequest(

        @NotBlank(message = "Account number is required")
        String accountNumber

) {
}
