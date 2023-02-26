package com.wazzup.exception;

public class CookieNotFoundException extends RuntimeException {
    private static final String MSG = "Cookie not found";

    public CookieNotFoundException() {
        super(MSG);
    }
}
