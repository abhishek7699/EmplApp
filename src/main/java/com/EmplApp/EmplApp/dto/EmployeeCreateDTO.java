package com.EmplApp.EmplApp.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record EmployeeCreateDTO(

        @NotBlank(message = "Name is required")
        @Size(min=2,max=50,message ="Name must be 2-50 characters")
        String name,

        @NotBlank(message = "Email is required")
        @Size(min=2,max=50,message ="Name must be 2-50 characters")
        @Email(message="Must be valid email")
        String email,

        @NotBlank(message = "dept is required")
        @Size(min=2,max=50,message ="Name must be 2-50 characters")
        String dept,

        @NotBlank(message = "Designation is required")
        @Size(min=2,max=50,message ="Name must be 2-50 characters")
        String designation,

        @NotNull(message = "SSN is required")
        @Digits(integer = 9, fraction = 0, message = "SSN must be exactly 9 digits")
        @Min(value = 100000000L, message = "SSN must be a 9-digit number (no leading zeros allowed in value)")
        @Max(value = 999999999L, message = "SSN must be a 9-digit number")
        Long ssn,

        @NotNull(message = "DATE is required")
        @Past
        LocalDate dob

) { }
