package com.app.pharmacy.domain.dto.employee;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;
import java.time.LocalDate;


public record CreateEmployeeRequest(
        @NotBlank(message = "username is mandatory field")
        String username,
        @NotNull(message = "role is mandatory field")
        EmployeeRole role,
        @NotBlank(message = "password is mandatory field")
        String password,
        @NotBlank(message = "firstName is mandatory field")
        String firstName,
        String lastName,
        LocalDate birthDate,
        @Min(value = 0, message = "age cannot be negative")
        Integer age,
        @NotBlank(message = "sex is mandatory field")
        @Pattern(regexp = "[FM]", message = "sex should be one of F or M")
        String sex,
        @NotBlank(message = "type is mandatory field")
        String type,
        String address,
        String mail,
        @NotBlank(message = "phoneNo is mandatory field")
        String phoneNo,
        BigDecimal salary

) {
}
