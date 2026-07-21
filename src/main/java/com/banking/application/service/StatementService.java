package com.banking.application.service;

import com.banking.application.dto.EmailDetails;
import com.banking.application.dto.TransactionResponse;
import com.banking.application.entity.Transaction;
import com.banking.application.entity.User;
import com.banking.application.exception.ResourceNotFoundException;
import com.banking.application.repository.TransactionRepository;
import com.banking.application.repository.UserRepository;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;


import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class StatementService {


    private final TransactionRepository transactionRepository;

    private final UserRepository userRepository;

    private final EmailService emailService;


    @Value("${statement.file.location}")
    private String statementLocation;

    /**
     * 1. retrieve list of transactions within a date range given an account number
     * 2. generate a pdf file of transactions
     * 3. send the file via email
     */

    public Page<TransactionResponse> generateStatement(
            String accountNumber,
            String startDate,
            String endDate,
            int page,
            int size,
            String sortBy,
            String direction
    ) throws Exception {

        LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE);
        LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ISO_DATE);

        User userAccount = userRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User with account number " + accountNumber + " not found")
                );

        String customerName = userAccount.getFirstName() + " " + userAccount.getLastName()
                + (userAccount.getOtherName() != null ? " " + userAccount.getOtherName() : "");

        //New Way with pagination and sorting
        Sort sort = direction.equalsIgnoreCase("DESC")
                ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        // For API response — paginated
        Page<Transaction> transactionPage = transactionRepository
                .findByAccountNumberAndCreatedAtBetween(accountNumber, start, end, pageable);

        //  For PDF — fetch all, no pagination
        List<Transaction> transactionsForPdf  = transactionRepository
                .findByAccountNumberAndCreatedAtBetween(accountNumber, start, end, Pageable.unpaged())
                .getContent();


        generatePdf(
                userAccount,
                customerName,
                startDate,
                endDate,
                transactionsForPdf
        );

        // Send Statement as an email attachment
        EmailDetails emailDetails = new EmailDetails(
                userAccount.getEmail(),
                "Kindly find your requested account statement attached!",
                "STATEMENT OF ACCOUNT",
                statementLocation
        );

        emailService.sendEMailWithAttachment(emailDetails);

        return transactionPage.map(transaction -> new TransactionResponse(

                UUID.fromString(transaction.getTransactionId()),
                transaction.getTransactionType(),
                transaction.getAmount(),
                transaction.getAccountNumber(),
                transaction.getStatus(),
                transaction.getCreatedAt()

                )
        );
    }


    public void generatePdf(
            User user,
            String customerName,
            String startDate,
            String endDate,
            List<Transaction> transactionsForPdf
    ) throws Exception {

        Rectangle statementSize = new Rectangle(PageSize.A4);
        Document document = new Document(statementSize);

        log.info("Setting size of document");

        OutputStream outputStream = new FileOutputStream(statementLocation);

        PdfWriter.getInstance(document, outputStream);

        document.open();

        // HEADING TABLE 1
        PdfPTable bankInfoTable = new PdfPTable(1);
        PdfPCell bankName = new PdfPCell(new Phrase("Banking App"));
        bankName.setBorder(0);
        bankName.setBackgroundColor(BaseColor.BLUE);
        bankName.setPadding(10f);

        PdfPCell bankAddress = new PdfPCell(new Phrase("23, Dummy Banking Address Street, India"));
        bankAddress.setBorder(0);

        bankInfoTable.addCell(bankName);
        bankInfoTable.addCell(bankAddress);


        // HEADING TABLE 2
        PdfPTable statementInfoTable = new PdfPTable(2);
        PdfPCell customerInfo = new PdfPCell(new Phrase("Start Date: " + startDate));
        customerInfo.setBorder(0);

        PdfPCell statement = new PdfPCell(new Phrase("STATEMENT OF ACCOUNT"));
        statement.setBorder(0);

        PdfPCell stopDate = new PdfPCell(new Phrase("End Date: " + endDate));
        stopDate.setBorder(0);

        PdfPCell name = new PdfPCell(new Phrase("Customer Name: " + customerName));
        name.setBorder(0);

        PdfPCell space = new PdfPCell();
        space.setBorder(0);

        PdfPCell address = new PdfPCell(new Phrase("Customer Address: " + user.getAddress()));
        address.setBorder(0);

        statementInfoTable.addCell(customerInfo);
        statementInfoTable.addCell(statement);
        statementInfoTable.addCell(stopDate);
        statementInfoTable.addCell(name);
        statementInfoTable.addCell(space);
        statementInfoTable.addCell(address);


        // TRANSACTIONS TABLE
        PdfPTable transactionsTable = new PdfPTable(4);
        PdfPCell date = new PdfPCell(new Phrase("DATE"));
        date.setBackgroundColor(BaseColor.BLUE);
        date.setBorder(0);

        PdfPCell transactionType = new PdfPCell(new Phrase("TYPE"));
        transactionType.setBackgroundColor(BaseColor.BLUE);
        transactionType.setBorder(0);

        PdfPCell transactionAmount = new PdfPCell(new Phrase("AMOUNT"));
        transactionAmount.setBackgroundColor(BaseColor.BLUE);
        transactionAmount.setBorder(0);

        PdfPCell transactionStatus = new PdfPCell(new Phrase("STATUS"));
        transactionStatus.setBackgroundColor(BaseColor.BLUE);
        transactionStatus.setBorder(0);

        transactionsTable.addCell(date);
        transactionsTable.addCell(transactionType);
        transactionsTable.addCell(transactionAmount);
        transactionsTable.addCell(transactionStatus);

        transactionsForPdf.forEach(transaction -> {
            transactionsTable.addCell(new Phrase(transaction.getCreatedAt().toString()));
            transactionsTable.addCell(new Phrase(transaction.getTransactionType()));
            transactionsTable.addCell(new Phrase(transaction.getAmount().toString()));
            transactionsTable.addCell(new Phrase(transaction.getStatus()));
        });


        document.add(bankInfoTable);
        document.add(statementInfoTable);
        document.add(transactionsTable);

        document.close();

        log.info("Bank statement generated for {}", user.getEmail());
    }
}
