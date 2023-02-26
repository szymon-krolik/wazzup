package com.wazzup.exception;

public class UserAccountLockedException extends RuntimeException {
    private static final String MSG = "Your account is locked, please contact with administartion";

    public UserAccountLockedException() {
        super(MSG);
    }
}
