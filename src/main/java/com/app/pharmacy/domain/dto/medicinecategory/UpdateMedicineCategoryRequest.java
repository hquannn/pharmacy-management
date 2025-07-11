package com.app.pharmacy.domain.dto.medicinecategory;

public record UpdateMedicineCategoryRequest(
        String name,
        String description
) {
}
