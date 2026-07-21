package com.banking.application.service.impl;

import com.banking.application.dto.TransactionRequest;
import com.banking.application.entity.Transaction;
import com.banking.application.repository.TransactionRepository;
import com.banking.application.service.TransactionService;
import com.banking.application.dto.TransactionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    TransactionRepository transactionRepository;

    @Override
    public TransactionResponse saveTransaction (TransactionRequest transactionRequest) {
        Transaction transaction = Transaction.builder()
                .transactionType(transactionRequest.getTransactionType())
                .accountNumber(transactionRequest.getAccountNumber())
                .amount(transactionRequest.getAmount())
                .status("SUCCESS")
                .build();

        Transaction saved = transactionRepository.save(transaction);
        System.out.println("Transaction saved successfully!");

        return new TransactionResponse(
                saved.getTransactionId(),
                saved.getTransactionType(),
                saved.getAmount(),
                saved.getAccountNumber(),
                saved.getStatus(),
                saved.getCreatedAt()
        );
    }
}
