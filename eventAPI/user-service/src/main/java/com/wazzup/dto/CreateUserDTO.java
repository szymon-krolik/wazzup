package com.wazzup.dto;

import com.wazzup.entity.UserRole;
import lombok.*;

import java.time.LocalDate;
import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserDTO {
    private String email;
    private String phoneNumber;
    private String name;
    private String password;
    private String matchingPassword;
    private String birthDateS;
    private String city;
    private List<UserRole> userRole;
    private LocalDate birthDate;



}
