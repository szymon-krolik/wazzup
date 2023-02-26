package com.wazzup.exception;

public class LinkExpiredException extends RuntimeException {
    private static final String MSG = "Link expired";

    public LinkExpiredException() {
        super(MSG);
    }
}
