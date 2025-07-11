package com.app.pharmacy.domain.dto.medicine;

import jakarta.validation.constraints.DecimalMin;

import java.math.BigDecimal;


public record UpdateMedicineRequest(
        String name,
        @DecimalMin(value = "0.0", message = "price cannot be negative")
        BigDecimal price,
        String categoryId,
        String medicineUnitId
) {
}
