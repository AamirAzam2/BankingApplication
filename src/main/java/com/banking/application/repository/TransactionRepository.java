package com.banking.application.repository;

import com.bank.application.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.time.LocalDate;

public interface TransactionRepository extends JpaRepository<Transaction, String> {

    Page<Transaction> findByAccountNumberAndCreatedAtBetween(
            String accountNumber,
            LocalDate startDate,
            LocalDate endDate,
            Pageable pageable
    );
}
