package com.wazzup.exception;

public class UserAccountIsNotActiveException extends RuntimeException {
    private static final String MSG = "Please activate your account";

    public UserAccountIsNotActiveException() {
        super(MSG);
    }
}
