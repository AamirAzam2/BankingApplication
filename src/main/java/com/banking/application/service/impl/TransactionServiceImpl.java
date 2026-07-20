package main.java.com.banking.application.service.impl;

import com.banking.banking_app_apis.dto.TransactionDto;
import com.bank.application.entity.Transaction;
import com.bank.application.repository.TransactionRepository;
import com.bank.application.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    TransactionRepository transactionRepository;

    @Override
    public TransactionResponse  saveTransaction (TransactionRequest transactionRequest) {
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
