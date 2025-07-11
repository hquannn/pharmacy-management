package com.app.pharmacy.domain.dto.customer;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;

public record UpdateCustomerRequest(
        String firstName,
        String lastName,
        @Min(value = 0, message = "age cannot be negative")
        Integer age,
        @Pattern(regexp = "[FM]", message = "sex should be one of F or M")
        String sex,
        String phoneNo,
        String mail
) {
}
