package com.banking.application.controller;

import com.banking.application.dto.*;
import com.banking.application.service.impl.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Tag(name = "User Account Management APIs")
public class UserController {

    private final UserService userService;


    @PostMapping
    @Operation(
            summary = "Create New User Account",
            description = "Create a new user and assign an account ID"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Account created successfully"
    )
    public ResponseEntity<BankResponse> createAccount(
            @Valid @RequestBody UserRequest userRequest) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.createAccount(userRequest));
    }

    @PostMapping("/login")
    @Operation(
            summary = "User Login",
            description = "Authenticate user and generate token"
    )
    public ResponseEntity<BankResponse> login(
            @Valid @RequestBody LoginRequest loginRequest) {

        return ResponseEntity.ok(
                userService.login(loginRequest)
        );
    }


    @GetMapping("/balance")
    @Operation(
            summary = "Balance Enquiry",
            description = "Check account balance using account number"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Balance retrieved successfully"
    )
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<BankResponse> balanceEnquiry(
            @RequestParam String accountNumber) {

        EnquiryRequest enquiryRequest =
                new EnquiryRequest(accountNumber);

        return ResponseEntity.ok(
                userService.balanceEnquiry(enquiryRequest)
        );
    }


    @GetMapping("/name")
    @Operation(
            summary = "Name Enquiry",
            description = "Retrieve account holder name"
    )
    public ResponseEntity<NameEnquiryResponse> nameEnquiry(
            @RequestParam String accountNumber) {

        EnquiryRequest enquiryRequest =
                new EnquiryRequest(accountNumber);

        return ResponseEntity.ok(
                userService.nameEnquiry(enquiryRequest)
        );
    }


    @PostMapping("/credit")
    @Operation(
            summary = "Credit Amount",
            description = "Add money to account"
    )
    public ResponseEntity<BankResponse> creditAmount(
            @Valid @RequestBody CreditDebitRequest request) {

        return ResponseEntity.ok(
                userService.creditAmount(request)
        );
    }


    @PostMapping("/debit")
    @Operation(
            summary = "Debit Amount",
            description = "Withdraw money from account"
    )
    public ResponseEntity<BankResponse> debitAmount(
            @Valid @RequestBody CreditDebitRequest request) {

        return ResponseEntity.ok(
                userService.debitAmount(request)
        );
    }


    @PostMapping("/transfer")
    @Operation(
            summary = "Transfer Amount",
            description = "Transfer money between accounts"
    )
    public ResponseEntity<BankResponse> transfer(
            @Valid @RequestBody TransferRequest request) {

        return ResponseEntity.ok(
                userService.transfer(request)
        );
    }
}
