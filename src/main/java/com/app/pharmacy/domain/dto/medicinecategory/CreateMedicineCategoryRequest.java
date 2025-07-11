package com.app.pharmacy.domain.dto.medicinecategory;

import jakarta.validation.constraints.NotBlank;

public record CreateMedicineCategoryRequest(
        @NotBlank(message = "name is mandatory field")
        String name,
        @NotBlank(message = "description is mandatory field")
        String description
) {
}
