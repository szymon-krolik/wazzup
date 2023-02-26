package com.wazzup.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class ExceptionDetailsDTO {
    private Date timeStamp;
    private String message;
    private String details;
}
