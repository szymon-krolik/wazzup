package com.wazzup.enums;

import lombok.Getter;

public enum ParameterCodeE {
    CZAS_WAZN_LINKU("CZAS_WAZN_LINK_AKT");

    @Getter
    private final String description;
    ParameterCodeE(String description) {
        this.description = description;
    }

    public static ParameterCodeE getValue(String x) {
        ParameterCodeE parameterCodeE = null;
        for (ParameterCodeE type : ParameterCodeE.values()) {
            if (type.getDescription().equals(x))
                parameterCodeE = type;
        }
        return parameterCodeE;
    }
}
