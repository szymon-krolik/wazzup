package com.wazzup.exception;

public class SessionNotFoundException extends RuntimeException {
    private static final String MSG = "Session not found";

    public SessionNotFoundException() {
        super(MSG);
    }
}
