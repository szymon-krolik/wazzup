package com.wazzup.common.dto;

import com.wazzup.common.entity.Parameter;
import lombok.Data;

@Data
public class ParameterDTO {
    private String code;
    private int value;

    public static ParameterDTO of (Parameter parameter) {
        ParameterDTO dto = new ParameterDTO();

        dto.setCode(parameter.getCode());
        dto.setValue(parameter.getValue());

        return dto;
    }
}
