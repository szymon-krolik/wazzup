package com.wazzup.controller;


import com.wazzup.dto.LoginUserDTO;
import com.wazzup.dto.UserInformationDTO;
import com.wazzup.dto.CreateUserDTO;

import com.wazzup.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Tag(name = "User Managment")
@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
public class UserController {

    private final UserService USER_SERVICE;

    @Operation(summary = "Create new user account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success create new account"),
            @ApiResponse(responseCode = "409", description = "User already exist")
    })
    @ResponseStatus(HttpStatus.OK)
    @PostMapping
    public void createUser(@RequestBody CreateUserDTO createUserDTO, HttpServletRequest request, HttpServletResponse response) {
        USER_SERVICE.createUser(createUserDTO, request, response);
    }

    @Operation(summary = "Activation user account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success activate account"),
            @ApiResponse(responseCode = "423", description = "Activaction link expired")
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/confirm")
    public void confirmUser(@RequestParam("token") String token, HttpServletResponse response, HttpServletRequest request) {
         USER_SERVICE.confirmUser(token, response, request);
    }

    @Operation(summary = "Login user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success login"),
            @ApiResponse(responseCode = "404", description = "Incorrect credentials")
    })
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/login")
    public UserInformationDTO loginUser(@RequestBody LoginUserDTO loginUserDTO, HttpServletResponse response) {
        return USER_SERVICE.loginUser(loginUserDTO, response);
    }

    @Operation(summary = "User profile information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success get user data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("isAuthenticated() && hasAuthority('USER')")
    @GetMapping("/profile")
    public UserInformationDTO userProfile() {
        return USER_SERVICE.getUserProfile();
    }

    @Operation(summary = "Resend activate account link")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success send link"),
            @ApiResponse(responseCode = "400", description = "User not found")
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/resend-confirmation-mail")
    public void rensedActivationLink(HttpServletRequest httpServletRequest) {
        USER_SERVICE.resendActivationLink(httpServletRequest);
    }

}
