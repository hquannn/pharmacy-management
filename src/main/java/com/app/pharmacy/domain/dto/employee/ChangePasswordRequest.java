package com.app.pharmacy.domain.dto.employee;

import jakarta.validation.constraints.NotBlank;

public record ChangePasswordRequest(
        @NotBlank(message = "oldPassword is mandatory field")
        String oldPassword,
        @NotBlank(message = "newPassword is mandatory field")
        String newPassword,
        @NotBlank(message = "confirmNewPassword is mandatory field")
        String confirmNewPassword
) {
}
