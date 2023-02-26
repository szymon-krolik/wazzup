package com.wazzup.exception;

public class UserAlreadyExistException extends RuntimeException{
    private static final String MSG = "User already exist";

    public UserAlreadyExistException(){
        super(MSG);
    }
}

