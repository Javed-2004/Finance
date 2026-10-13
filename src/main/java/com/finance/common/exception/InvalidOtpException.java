package com.finance.common.exception;

public class InvalidOtpException
        extends RuntimeException {

    public InvalidOtpException(String message) {
        super(message);
    }
}