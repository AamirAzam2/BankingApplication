package com.banking.application.service;

import com.banking.application.dto.TransactionRequest;
import com.banking.application.dto.TransactionResponse;

public interface TransactionService {

    TransactionResponse saveTransaction(TransactionRequest transactionRequest);
}
