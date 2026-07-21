package com.banking.application.controller;

import com.banking.application.dto.TransactionResponse;
import com.banking.application.service.StatementService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/bankStatement")
@RequiredArgsConstructor
@Tag(name = "Bank Statement APIs")
public class TransactionController {
    private final StatementService statementService;

    @GetMapping
    public ResponseEntity<Page<TransactionResponse>> generateBankStatement(

            @RequestParam String accountNumber,
            @RequestParam String startDate,
            @RequestParam String endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction

    ) throws Exception {


        return ResponseEntity.ok(
                statementService.generateStatement(
                        accountNumber,
                        startDate,
                        endDate,
                        page,
                        size,
                        sortBy,
                        direction
                )
        );
    }
}
