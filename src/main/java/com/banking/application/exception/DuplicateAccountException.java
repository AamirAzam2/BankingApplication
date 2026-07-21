package com.banking.application.exception;

public class DuplicateAccountException extends RuntimeException {

    public DuplicateAccountException(String message) {
        super(message);
    }
}
