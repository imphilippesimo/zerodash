package com.zerofiltre.zerodash.utils.error;

public class AuthenticationRequestNotValidException extends RuntimeException {

    public AuthenticationRequestNotValidException(String message) {
        super(message);
    }
}
