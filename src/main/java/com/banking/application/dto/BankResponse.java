package com.banking.application.dto;

public record BankResponse(

        String responseCode,

        String responseMessage,

        AccountInfo accountInfo

) { }
