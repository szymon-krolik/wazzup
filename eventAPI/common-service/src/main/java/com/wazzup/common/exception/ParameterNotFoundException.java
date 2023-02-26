package com.wazzup.common.exception;

public class ParameterNotFoundException extends RuntimeException {
    private static final String MSG = "Parameter not found";

    public ParameterNotFoundException() {
        super(MSG);
    }
}
