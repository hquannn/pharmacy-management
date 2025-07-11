package com.app.pharmacy.domain.dto.purchase;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UpdatePurchaseRequest(
        String medicineId,
        LocalDate mfgDate,
        String supplierId,
        @Min(value = 1, message = "quantity should be greater than 0")
        Integer quantity,
        @DecimalMin(value = "0.0", message = "cost should not be negative")
        BigDecimal cost,
        String locationRackId,
        LocalDate expDate
) {
}
