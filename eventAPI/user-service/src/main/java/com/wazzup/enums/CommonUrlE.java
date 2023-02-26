package com.wazzup.enums;

import lombok.Getter;

public enum CommonUrlE {
    GET_PARAMETER_BY_VALUE("/parameter/by-code");

    @Getter
    private String url;

    CommonUrlE(String url) {
        this.url = url;
    }
}
