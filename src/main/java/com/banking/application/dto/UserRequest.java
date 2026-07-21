package com.banking.application.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UserRequest(

        @NotBlank(message = "First name is required")
        String firstName,

        @NotBlank(message = "Last name is required")
        String lastName,

        String otherName,

        @NotBlank(message = "Gender is required")
        String gender,

        @NotBlank(message = "Address is required")
        String address,

        String stateOfOrigin,

        @NotBlank(message = "Email is required")
        @Email(message = "Email should be valid")
        String email,

        @NotBlank(message = "Password is required")
        String password,

        @NotBlank(message = "Phone number is required")
        @Pattern(
                regexp = "^[0-9]{10,15}$",
                message = "Phone number must contain 10-15 digits"
        )
        String phoneNumber,

        String alternativePhoneNumber

) {

