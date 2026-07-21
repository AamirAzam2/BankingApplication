package com.banking.application.service.impl;

import java.util.UUID;

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
                .transactionType(transactionRequest.transactionType())
                .accountNumber(transactionRequest.accountNumber())
                .amount(transactionRequest.amount())
                .status("SUCCESS")
                .build();

        Transaction saved = transactionRepository.save(transaction);
        System.out.println("Transaction saved successfully!");

        return new TransactionResponse(
                UUID.fromString(saved.getTransactionId()),
                saved.getTransactionType(),
                saved.getAmount(),
                saved.getAccountNumber(),
                saved.getStatus(),
                saved.getCreatedAt()
        );
    }
}
