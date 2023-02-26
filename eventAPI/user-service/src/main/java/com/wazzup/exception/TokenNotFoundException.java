package com.wazzup.exception;

/**
 * @author Szymon Królik
 */
public class TokenNotFoundException extends RuntimeException{
    private static final String MSG = "Can't find token";

    public TokenNotFoundException() {
        super(MSG);
    }
}
