package com.banking.application.service.impl;

import com.banking.application.config.JwtTokenProvider;
import com.banking.application.entity.Role;
import com.banking.application.entity.User;
import com.banking.application.dto.*;
import com.banking.application.exception.DuplicateAccountException;
import com.banking.application.exception.ResourceNotFoundException;
import com.banking.application.repository.UserRepository;
import com.banking.application.service.EmailService;
import com.banking.application.service.TransactionService;
import com.banking.application.service.UserService;
import com.banking.application.utils.AccountUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final TransactionService transactionService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Creating an account - saving a new user into the db
     * Check if user already has an account
     */
    @Override
    @Transactional
    public BankResponse createAccount(UserRequest userRequest) {

        if(userRepository.existsByEmail(userRequest.email())) {
            throw new DuplicateAccountException("Account with this email already exists: "
                    + userRequest.email());
        }

        User newUser = User.builder()
                .firstName(userRequest.firstName())
                .lastName(userRequest.lastName())
                .otherName(userRequest.otherName())
                .gender(userRequest.gender())
                .address(userRequest.address())
                .stateOfOrigin(userRequest.stateOfOrigin())
                .accountNumber(AccountUtils.generateAccountNumber())
                .accountBalance(BigDecimal.ZERO)
                .email(userRequest.email())
                .password(passwordEncoder.encode(userRequest.password()))
                .phoneNumber(userRequest.phoneNumber())
                .alternativePhoneNumber(userRequest.alternativePhoneNumber())
                .status("ACTIVE")
                .role(Role.ROLE_USER)
                .build();

        User savedUser = userRepository.save(newUser);

        // Send email alert
        String fullName = savedUser.getFirstName() + " "
                + savedUser.getLastName()
                + (savedUser.getOtherName() != null ? " " + savedUser.getOtherName() : "");

        EmailDetails emailDetails = new EmailDetails(
                savedUser.getEmail(),
                """
                        Congratulations! Your account has been successfully created.
                        Account Details:
                        Account Name: %s
                        Account Number: %s
                        """.formatted(
                        fullName,
                        savedUser.getAccountNumber()
                ),
                "ACCOUNT CREATION",
                null
        );

        emailService.sendEmailAlert(emailDetails);

        return new BankResponse(
                AccountUtils.ACCOUNT_CREATION_SUCCESS_CODE,
                AccountUtils.ACCOUNT_CREATION_SUCCESS_MESSAGE,
                new AccountInfo(
                        fullName,
                        savedUser.getAccountBalance(),
                        savedUser.getAccountNumber()
                )
        );
    }


    public LoginResponse login(LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.email(),
                        loginRequest.password()
                )
        );

        // Send Login Email
        EmailDetails loginAlert = new EmailDetails(
                loginRequest.email(),
                """
                        You logged into your account.
                        If you did not initiate this request,
                        please contact your bank.
                        """,

                "You're logged in!",
                null
        );

        emailService.sendEmailAlert(loginAlert);

        return new LoginResponse(
                jwtTokenProvider.generateToken(authentication),
                "Login successful"
        );
    }


    /**
     * Balance Enquiry
     * Name Enquiry
     * Credit
     * Debit
     * Transfer
     */
    @Override
    public BankResponse balanceEnquiry(EnquiryRequest enquiryRequest) {

        // Check if the provided account number exists in the DB
        boolean isAccountExists = userRepository.existsByAccountNumber(enquiryRequest.accountNumber());
        if(!isAccountExists) {
            throw new ResourceNotFoundException("Account not found with account number: "
                    + enquiryRequest.accountNumber());
        }

        User foundUser = userRepository.findByAccountNumber(enquiryRequest.accountNumber())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Account not found: " + enquiryRequest.accountNumber()
                ));

        String fullName = foundUser.getFirstName() + " "
                + foundUser.getLastName()
                + (foundUser.getOtherName() != null ? " " + foundUser.getOtherName() : "");

        return new BankResponse(
                AccountUtils.ACCOUNT_FOUND_CODE,
                AccountUtils.ACCOUNT_FOUND_MESSAGE,
                new AccountInfo(
                        fullName,
                        foundUser.getAccountBalance(),
                        foundUser.getAccountNumber()
                )
        );

    }

    @Override
    public NameEnquiryResponse nameEnquiry (
            EnquiryRequest enquiryRequest) {


        User user = userRepository.findByAccountNumber(enquiryRequest.accountNumber())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Account not found with account number: "
                                        + enquiryRequest.accountNumber()
                        )
                );


        String fullName =
                Stream.of(
                                user.getFirstName(),
                                user.getLastName(),
                                user.getOtherName()
                        )
                        .filter(Objects::nonNull)
                        .collect(Collectors.joining(" "));


        return new NameEnquiryResponse(
                user.getAccountNumber(),
                fullName
        );
    }


    @Override
    @Transactional
    public BankResponse creditAmount(CreditDebitRequest creditDebitRequest) {


        User user = userRepository.findByAccountNumber(creditDebitRequest.accountNumber())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Account not found"
                                ));

        user.setAccountBalance(user.getAccountBalance().add(creditDebitRequest.amount()));

        userRepository.save(user);

        transactionService.saveTransaction(
                new TransactionRequest(
                        "CREDIT",
                        creditDebitRequest.amount(),
                        creditDebitRequest.accountNumber(),
                        "SUCCESS"
                )
        );

        return new BankResponse(
                AccountUtils.ACCOUNT_CREDITED_SUCCESS_CODE,
                AccountUtils.ACCOUNT_CREDITED_SUCCESS_MESSAGE,
                new AccountInfo(
                        user.getFirstName(),
                        user.getAccountBalance(),
                        user.getAccountNumber()
                )
        );
    }



    @Override
    @Transactional
    public BankResponse debitAmount(CreditDebitRequest request) {


        User user = userRepository.findByAccountNumber(request.accountNumber())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Account not found"
                                ));



        if(user.getAccountBalance().compareTo(request.amount()) < 0) {

            throw new RuntimeException("Insufficient balance");
        }

        user.setAccountBalance(user.getAccountBalance().subtract(request.amount()));

        userRepository.save(user);

        transactionService.saveTransaction(
                new TransactionRequest(
                        "DEBIT",
                        request.amount(),
                        request.accountNumber(),
                        "SUCCESS"
                )
        );

        return new BankResponse(
                AccountUtils.ACCOUNT_DEBITED_SUCCESS_CODE,
                AccountUtils.ACCOUNT_DEBITED_SUCCESS_MESSAGE,
                new AccountInfo(
                        user.getFirstName(),
                        user.getAccountBalance(),
                        user.getAccountNumber()
                )
        );
    }


    @Override
    @Transactional
    public BankResponse transfer (TransferRequest request) {

        User source = userRepository.findByAccountNumber(request.sourceAccountNumber())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Source account not found"
                        ));


        User destination = userRepository.findByAccountNumber(request.destinationAccountNumber())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Destination account not found"
                        ));

        if(source.getAccountBalance().compareTo(request.amount()) < 0) {

            throw new RuntimeException(
                    "Insufficient balance"
            );
        }

        source.setAccountBalance(source.getAccountBalance().subtract(request.amount()));

        destination.setAccountBalance(destination.getAccountBalance().add(request.amount())
        );

        userRepository.save(source);

        userRepository.save(destination);

        transactionService.saveTransaction(
                new TransactionRequest(
                        "TRANSFER_DEBIT",
                        request.amount(),
                        request.sourceAccountNumber(),
                        "SUCCESS"
                )
        );

        transactionService.saveTransaction(
                new TransactionRequest(
                        "TRANSFER_CREDIT",
                        request.amount(),
                        request.destinationAccountNumber(),
                        "SUCCESS"
                )
        );

        return new BankResponse(
                AccountUtils.TRANSFER_SUCCESS_CODE,
                AccountUtils.TRANSFER_SUCCESS_MESSAGE,
                new AccountInfo(
                        source.getFirstName(),
                        source.getAccountBalance(),
                        source.getAccountNumber()
                )
        );
    }
}
