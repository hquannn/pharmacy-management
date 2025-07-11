package com.app.pharmacy.domain.dto.sale;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record SaleItemRequest(
        @NotBlank(message = "inventoryId is mandatory field")
        String inventoryId,
        @NotNull(message = "quantity is mandatory field")
        @Min(value = 1, message = "quantity should be greater than 0")
        Integer quantity,
        @NotNull(message = "price is mandatory field")
        @DecimalMin(value = "0.0", message = "price should not be negative")
        BigDecimal price
) {
}
