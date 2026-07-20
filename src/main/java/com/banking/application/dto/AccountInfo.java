package main.java.com.banking.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

import java.math.BigDecimal;

public record AccountInfo(

        String accountName,

        BigDecimal accountBalance,

        String accountNumber

) {
}
