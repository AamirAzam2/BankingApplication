package com.banking.application.service;

public interface UserService {

    BankResponse createAccount(UserRequest userRequest);

    BankResponse login(LoginDto loginDto);

    BankResponse balanceEnquiry(EnquiryRequest enquiryRequest);

    NameEnquiryResponse nameEnquiry(EnquiryRequest enquiryRequest);

    BankResponse creditAmount(CreditDebitRequest creditDebitRequest);

    BankResponse debitAmount(CreditDebitRequest creditDebitRequest);

    BankResponse transfer(TransferRequest transferRequest);
}
