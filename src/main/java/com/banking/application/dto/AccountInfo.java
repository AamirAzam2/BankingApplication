package main.java.com.banking.application.dto;

import java.math.BigDecimal;

public record AccountInfo(

        String accountName,

        BigDecimal accountBalance,

        String accountNumber

) {
}
