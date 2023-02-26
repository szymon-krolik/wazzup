package com.wazzup.exception;

/**
 * @author Szymon Królik
 */
public class UserNotFoundException extends RuntimeException{
    private static final String MSG = "Can't find user";

    public UserNotFoundException() {
        super(MSG);
    }
}
