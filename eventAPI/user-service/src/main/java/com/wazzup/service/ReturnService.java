package com.wazzup.service;

import com.wazzup.entity.RefreshToken;
import lombok.Data;

import java.util.Collections;
import java.util.Map;

@Data
public class ReturnService<T> {
    private String message;
    private int status;
    private Map<String, String> validationError;
    private T value;
    private RefreshToken refreshToken;


    public static <T> ReturnService returnError(String errMsg, T obj, int status) {
        ReturnService ret = new ReturnService();
        ret.setValidationError(Collections.singletonMap("user", errMsg));
        ret.setValue(obj);
        ret.setStatus(0);

        return ret;
    }

    public static <T> ReturnService returnError(String errMsg, Map<String, String> validationError, T obj, int status) {
        ReturnService ret = new ReturnService();
        ret.setValidationError(validationError);
        ret.setValue(obj);
        ret.setStatus(status);

        return ret;
    }

    public static ReturnService returnError(String errMsg, int status) {
        ReturnService ret = new ReturnService();
        ret.setMessage(errMsg);
        ret.setStatus(status);

        return ret;
    }

    public static <T> ReturnService returnInformation(String msg, T obj, int status) {
        ReturnService ret = new ReturnService();

        ret.setMessage(msg);
        ret.setStatus(status);
        ret.setValue(obj);

        return ret;
    }

    public static <T> ReturnService returnInformation(String msg, int status) {
        ReturnService ret = new ReturnService();

        ret.setMessage(msg);
        ret.setStatus(status);

        return ret;
    }

    public static <T> ReturnService returnRefreshTokenInformation(RefreshToken refreshToken, int status) {
        ReturnService ret = new ReturnService();

        ret.setRefreshToken(refreshToken);
        ret.setStatus(1);

        return ret;
    }
}
