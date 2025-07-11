package com.app.pharmacy.domain.dto.purchase;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreatePurchaseRequest(
        @NotBlank(message = "medicineId is mandatory field")
        String medicineId,
        @NotBlank(message = "supplierId is mandatory field")
        String supplierId,
        @NotBlank(message = "locationRackId is mandatory field")
        String locationRackId,
        @NotNull(message = "quantity is mandatory field")
        @Min(value = 1, message = "quantity should be greater than 0")
        Integer quantity,
        @NotNull(message = "cost is mandatory field")
        @DecimalMin(value = "0.0", message = "cost should not be negative")
        BigDecimal cost,
        @NotNull(message = "mfgDate is mandatory field")
        LocalDate mfgDate,
        @NotNull(message = "expDate is mandatory field")
        LocalDate expDate
) {
}
