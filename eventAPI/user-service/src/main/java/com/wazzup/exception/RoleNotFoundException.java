package com.wazzup.exception;

/**
 * @author Szymon Królik
 */
public class RoleNotFoundException extends RuntimeException{
    private static final String MSG = "Can't find role";

    public RoleNotFoundException() {
        super(MSG);
    }
}
