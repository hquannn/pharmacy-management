package com.app.pharmacy.domain.dto.customer;

import com.app.pharmacy.domain.dto.employee.EmployeeRole;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CreateCustomerRequest(

        @NotBlank(message = "firstName is mandatory field")
        String firstName,
        String lastName,
        @Min(value = 0, message = "age cannot be negative")
        Integer age,
        @NotBlank(message = "sex is mandatory field")
        @Pattern(regexp = "[FM]", message = "sex should be one of F or M")
        String sex,
        @NotBlank(message = "phoneNo is mandatory field")
        String phoneNo,
        String mail
) {
}
