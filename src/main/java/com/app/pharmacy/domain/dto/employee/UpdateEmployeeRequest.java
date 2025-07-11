package com.app.pharmacy.domain.dto.employee;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UpdateEmployeeRequest(
        String username,
        String firstName,
        String lastName,
        LocalDate birthDate,
        @Min(value = 0, message = "age cannot be negative")
        Integer age,
        @Pattern(regexp = "[FM]", message = "sex should be one of F or M")
        String sex,
        String type,
        String address,
        String mail,
        String phoneNo,
        BigDecimal salary
) {
}
