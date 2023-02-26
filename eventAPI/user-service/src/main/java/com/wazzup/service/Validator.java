package com.wazzup.service;



import com.wazzup.dto.CreateUserDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class Validator {
    private static Map<String, String> validationError = new HashMap<>();
    private final static int MIN_LENGTH = 3;

    public static Map<String, String> createUserValidator(CreateUserDTO dto) {

        if (ServiceFunctions.isNull(dto))
            return Collections.singletonMap("object", "Object cannot be null");

        if (!ServiceFunctions.isNull(dto.getEmail())) {
            if (!ServiceFunctions.validEmail(dto.getEmail()))
                validationError.put("email", "Please provide correct email address");
        } else {
            validationError.put("email", "Email cannot be null");
        }
        if (!ServiceFunctions.isNull(dto.getPhoneNumber())) {
            if (!ServiceFunctions.validPhoneNumber(dto.getPhoneNumber()))
                validationError.put("phoneNumber", "Please provide correct phone number");
        } else {
            validationError.put("phoneNumber", "Phone number cannot be null");
        }
        if (ServiceFunctions.isNull(dto.getName())) {
            validationError.put("name", "Name cannot be null");
        } else if (dto.getName().length() < MIN_LENGTH) {
            validationError.put("name", "Name should have at least 3 characters");
        }
        if (ServiceFunctions.isNull(dto.getPassword()))
            validationError.put("password", "Password should not be empty");
        if (ServiceFunctions.isNull(dto.getMatchingPassword()))
            validationError.put("matchingPassword", "Matching password should not be empty");

        if (!ServiceFunctions.isNull(dto.getPassword()) && !ServiceFunctions.isNull(dto.getMatchingPassword())) {
            if (!dto.getPassword().toLowerCase(Locale.ROOT).equals(dto.getMatchingPassword().toLowerCase()))
                validationError.put("password", "Passwords should match");
        }

        if (ServiceFunctions.isNull(dto.getBirthDateS())) {
            validationError.put("birthDate", "Birthdate cannot be null");
        }
//        if (!ServiceFunctions.isNull(dto.getBirthDate())) {
//            Date birthDate = ServiceFunctions.stringToDate(dto.getBirthDate());
//            LocalDateTime now = LocalDateTime.now().minusYears(18L);
//            if (!birthDate.before(ServiceFunctions.dateTimeToDate(now))) {
//                validationError.put("birthdate", "U are too young");
//            }
//        }

//        if (ServiceFunctions.isNull(dto.getCity()) || dto.getCity().length() < 2) {
//            validationError.put("city", "City cannot be null");
//        }

        return validationError;
    }
}
