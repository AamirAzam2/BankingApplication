package com.banking.application.service;

import com.banking.application.dto.BankResponse;
import com.banking.application.dto.CreditDebitRequest;
import com.banking.application.dto.EnquiryRequest;
import com.banking.application.dto.LoginRequest;
import com.banking.application.dto.LoginResponse;
import com.banking.application.dto.NameEnquiryResponse;
import com.banking.application.dto.TransferRequest;
import com.banking.application.dto.UserRequest;

public interface UserService {

    BankResponse createAccount(UserRequest userRequest);

    LoginResponse login(LoginRequest loginRequest);

    BankResponse balanceEnquiry(EnquiryRequest enquiryRequest);

    NameEnquiryResponse nameEnquiry(EnquiryRequest enquiryRequest);

    BankResponse creditAmount(CreditDebitRequest creditDebitRequest);

    BankResponse debitAmount(CreditDebitRequest creditDebitRequest);

    BankResponse transfer(TransferRequest transferRequest);
}
