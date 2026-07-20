package main.java.com.banking.application.service;

import com.banking.banking_app_apis.dto.TransactionDto;

public interface TransactionService {

    void saveTransaction(TransactionDto transactionDto);
}
