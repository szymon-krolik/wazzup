package com.wazzup.controller;



import com.wazzup.dto.ExceptionDetailsDTO;


import com.wazzup.exception.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice("userControllerAdvisor")
@RequiredArgsConstructor
public class ControllerAdvisor {
    @ExceptionHandler({UserAlreadyExistException.class})
    public ResponseEntity<?> handleConflictException(Exception exception, WebRequest request) {
        ExceptionDetailsDTO exceptionDetailsDTO = new ExceptionDetailsDTO(new Date(), exception.getMessage(), request.getDescription(false));
        return new ResponseEntity(exceptionDetailsDTO, HttpStatus.CONFLICT);
    }

    @ExceptionHandler({LinkExpiredException.class})
    public ResponseEntity<?> handleLockedException(Exception exception, WebRequest request) {
        ExceptionDetailsDTO exceptionDetailsDTO = new ExceptionDetailsDTO(new Date(), exception.getMessage(), request.getDescription(false));
        return new ResponseEntity(exceptionDetailsDTO, HttpStatus.LOCKED);
    }

    @ExceptionHandler({IncorrectCredentialsException.class, UserNotFoundException.class, SessionNotFoundException.class, CookieNotFoundException.class})
    public ResponseEntity<?> handleNotFoundException(Exception exception, WebRequest request) {
        ExceptionDetailsDTO exceptionDetailsDTO = new ExceptionDetailsDTO(new Date(), exception.getMessage(), request.getDescription(false));
        return new ResponseEntity(exceptionDetailsDTO, HttpStatus.NOT_FOUND);
    }
}
