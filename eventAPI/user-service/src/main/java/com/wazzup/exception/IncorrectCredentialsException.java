package com.wazzup.exception;

public class IncorrectCredentialsException extends RuntimeException {
    private static final String MSG = "Incorrect credentials";

    public IncorrectCredentialsException() {
        super(MSG);
    }
}
