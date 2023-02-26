package com.wazzup.dto;

import lombok.Data;

@Data
public class LoginUserDTO {
    private String email;
    private String phoneNumber;
    private String password;
}
